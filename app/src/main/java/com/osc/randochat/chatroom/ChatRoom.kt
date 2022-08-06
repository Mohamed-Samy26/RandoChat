package com.osc.randochat.chatroom


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.osc.randochat.R
import com.osc.randochat.adapters.MessageAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate.onBackPressed
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber
import java.io.File
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.util.*
import kotlin.math.abs

class ChatRoom : AppCompatActivity() {

    //local state and current user handle
//    private var calleeUser: User? = null
    private var currentMsg: String? = null
    private var mediaRecorder : MediaRecorder? = null
    private var recording = false
    private val storageRef = FirebaseStorage.getInstance().reference

    //Records
    private var recordFile = ""
    private val recordPath = this.getExternalFilesDir("/")!!.absolutePath
    private val isRecord: MutableMap<String, Boolean> = HashMap()


    //required data structures
    private var messageList = ArrayList<Message>()
    private val lastMessage: MutableMap<String, String> = HashMap()
    private val db = FirebaseFirestore.getInstance()
    private val userRef = db.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission(Manifest.permission.RECORD_AUDIO, 202)
        //to hide action bar
        supportActionBar?.hide()
        window.statusBarColor = getColor(R.color.statusbar)
        setContentView(R.layout.activity_chat_room)

        //initializing data
        val sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE)
        val phone = sharedPreferences.getString("phone", null)
        val name =  sharedPreferences.getString("name", null)
        val currentUser = User(name, phone)

        //get UI references
        val img = findViewById<CircleImageView>(R.id.recImg)
        val recName = findViewById<TextView>(R.id.TVname)
        val vidCall = findViewById<ImageButton>(R.id.vidCall)

        //Getting intent or saved instance data
        val extras = intent.extras
        if (extras == null) {
            finish()
        }
        else{
            Glide.with(this).load(extras.getString("image"))
                .placeholder(org.jitsi.meet.sdk.R.drawable.images_iconusers).into(img)
        }
        //database references
        val currentDoc = extras?.getString("room").toString()
        println(">>>>>>>>>>>>>>>>>>>>>> $currentDoc")
        val docRef = db.collection("chatRooms").document(currentDoc)
        val collRef = docRef.collection("Messages")

        updateLastSeen(currentUser.profile)
            recName.text = currentDoc
        recycler_chat.adapter = MessageAdapter(this , messageList , phone )
        recycler_chat.layoutManager = LinearLayoutManager(this)

        //listen to chat
        listenToChat(collRef)

        //Enable video calls
        vidCall.setOnClickListener { view: View? ->
           if (currentDoc.isNotEmpty()) {
               startVideoCall(currentDoc)
           }
           else Snackbar.make(view!!, "Invalid Room ID", BaseTransientBottomBar.LENGTH_SHORT)
               .show()
        }

        //Send button action
        sendBtn.setOnClickListener {
            //Remove extra spaces and break lines
            updateLastSeen(currentUser.profile)
            currentMsg = msgTxt.text.toString().trim { it <= ' ' }
            if(currentMsg?.isEmpty() == true){
               handleRecording(currentUser , collRef)
            }
            else if (currentMsg != "") {
                handleMessage(currentUser , collRef)
            }
        }
    }

    private fun updateState(msg: Message) {
        messageList.add(msg)
        recycler_chat.adapter!!.notifyItemInserted(messageList.size)
        recycler_chat!!.smoothScrollToPosition(recycler_chat.adapter!!.itemCount)
    }

    private fun encryptMsg(msg: String): String {
        return Base64.getEncoder()
            .encodeToString(msg.toByteArray(StandardCharsets.UTF_8))
    }

    private fun listenToChat(collRef: CollectionReference) {
        collRef.addSnapshotListener { snapshots: QuerySnapshot?, e: FirebaseFirestoreException? ->
            if (e != null) {
                Timber.tag(ContentValues.TAG).w(e, "listen:error")
                return@addSnapshotListener
            }
            if (snapshots != null) for (dc in snapshots.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        val msg = dc.document.toObject(
                            Message::class.java
                        )
                        try {
                            if (msg.time != messageList[messageList.size - 1].time) {
                                updateState(
                                    Message(
                                        String(Base64.getDecoder().decode(msg.text)),
                                        msg.time,
                                        String(Base64.getDecoder().decode(msg.user)),
                                        msg.isRecord
                                    )
                                )
                                println("msg >>>>>>>> " + msg.isRecord)
                            }
                        } catch (x: Exception) {
                            Timber.tag(ContentValues.TAG).d("Empty messages")
                        }
                        Timber.tag(ContentValues.TAG).d("New Message: %s", dc.document.data)
                    }
                    DocumentChange.Type.MODIFIED -> Timber.tag(ContentValues.TAG)
                        .d("Modified Message: %s", dc.document.data)
                    DocumentChange.Type.REMOVED -> Timber.tag(ContentValues.TAG)
                        .d("Removed Message: %s", dc.document.data)
                }
            }
        }
    }

    private fun updateLastSeen(user:String){
        val time = mutableMapOf<String , String>()
        time["lastSeen"] = System.currentTimeMillis().toString()
        userRef.document(user).set(time , SetOptions.merge())
    }

    @SuppressLint("SetTextI18n")
    private fun getLastSeen(user:String){
        try {
            userRef.document(user).get().addOnCompleteListener {
                val result:Long? = it.result.get("lastSeen").toString().toLongOrNull()
                 if(result == null){
                    lastSeen.text =  "Offline"
                }
                else {
                    val newRes = Timestamp(result).toString()
                        .substring(11 ,16)
                    if (abs(System.currentTimeMillis() - result) <= 60)
                        lastSeen.text ="Active now"
                    else lastSeen.text = "Last seen : $newRes"
            }}
        }
        catch (e : Exception){
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@ChatRoom, permission )
            == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@ChatRoom,
                arrayOf(permission),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this@ChatRoom, "Please Accept Permission" , Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 202) {
            if (!(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                onBackPressed()
            }
        }

    }
    private fun handleRecording(currentUser:User , collRef: CollectionReference){
        if (!recording){
            recording = true
            timer.visibility = View.VISIBLE
            timer.base = SystemClock.elapsedRealtime()
            timer.start()
            mediaRecorder = MediaRecorder()
            recordFile = "Recording${System.currentTimeMillis()}.3gp"
            mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder!!.setOutputFile("$recordPath/$recordFile")
            mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                mediaRecorder!!.prepare()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            mediaRecorder!!.start()
        }
        else {
            recording = false
            timer.visibility = View.INVISIBLE
            timer.stop()
            mediaRecorder!!.stop()
            mediaRecorder!!.release()
            mediaRecorder = null
            val randomKey = UUID.randomUUID().toString()
            val recordRef = storageRef.child("records/$randomKey")

            recordRef.putFile(Uri.fromFile(File("$recordPath/$recordFile")))
                .addOnSuccessListener {
                    println("onSuccess")
                    recordRef.downloadUrl.addOnSuccessListener { uri ->
                        val tmp = Message(
                            uri.toString(),
                            Timestamp(System.currentTimeMillis()).toString()
                            , currentUser.profile , true)
                        updateState(tmp)
                        //serialize encrypted message in the hashmap
                        lastMessage["text"] = encryptMsg(tmp.text)
                        lastMessage["time"] = tmp.time
                        lastMessage["user"] = encryptMsg(tmp.user)
                        isRecord["isRecord"] = true

                        //Send message hashmap to fireStore and handle success / failure
                        val msgDoc: String = Timestamp(System.currentTimeMillis())
                            .toString().replace("\\s".toRegex(), "")
                        collRef.document(msgDoc).set(isRecord)
                        collRef.document(msgDoc).set(lastMessage, SetOptions.merge())
                            .addOnSuccessListener {
                                Timber.tag(ContentValues.TAG).d("DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e: Exception? ->
                                Timber.tag(ContentValues.TAG).w(e, "Error writing document")
                            }
                        msgTxt.setText("")
                    }
                }.addOnFailureListener{
                    println("onFail" + it.message)
                }
        }
    }

    private fun handleMessage(currentUser:User , collRef: CollectionReference){
        // Add a new message object to the end of messages arraylist
        val tmp = Message(
            currentMsg,
            Timestamp(System.currentTimeMillis()).toString()
            , currentUser.profile , false
        )
        updateState(tmp)
        //serialize encrypted message in the hashmap
        lastMessage["text"] = encryptMsg(tmp.text)
        lastMessage["time"] = tmp.time
        lastMessage["user"] = encryptMsg(tmp.user)

        //Send message hashmap to fireStore and handle success / failure
        val msgDoc: String = Timestamp(System.currentTimeMillis())
            .toString().replace("\\s".toRegex(), "")
        collRef.document(msgDoc)
            .set(lastMessage, SetOptions.merge())
            .addOnSuccessListener {
                Timber.tag(ContentValues.TAG).d("DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e: Exception? ->
                Timber.tag(ContentValues.TAG).w(e, "Error writing document")
            }
        //empty the message holder to receive the next message
        msgTxt.setText("")
    }

    private fun startVideoCall(currentDoc:String ){
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom("RandoChatOSCcall$currentDoc")
                .setAudioMuted(false)
                .setVideoMuted(false)
                .setAudioOnly(false)
                .setConfigOverride("requireDisplayName", false)
                .build()
            JitsiMeetActivity.launch(this, options)
        }
}