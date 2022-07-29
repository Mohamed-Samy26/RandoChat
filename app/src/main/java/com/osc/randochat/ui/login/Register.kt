package com.osc.randochat.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.osc.randochat.R
import com.osc.randochat.helper.AnimateView
import com.osc.randochat.ui.MainActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class Register : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var storage = FirebaseStorage.getInstance()
    private lateinit var profile: CircleImageView
    private var storageRef = storage.reference.child("/Images/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        AnimateView.startAnimation(R.id.bg_reg, this, 2000)

        profile = findViewById(R.id.profile_image)
        val sign_up_btn = findViewById<Button>(R.id.signUp_btn2)
        val user_name = findViewById<EditText>(R.id.userName_et2)
        val phone = intent.getStringExtra("phone")

        if (phone!!.isEmpty()) {
            Toast.makeText(
                this,
                "Please enter a valid number", Toast.LENGTH_LONG
            ).show()
            finish()
        }

        val docRef = FirebaseFirestore.getInstance().collection("users")
            .document(phone)
        profile.setOnClickListener { pickImg() }
        sign_up_btn.setOnClickListener {
            val name = user_name.text.toString()
            if (!TextUtils.isEmpty(name)) {
                val profile: MutableMap<String, String?> = HashMap()
                profile["name"] = name
                profile["phone"] = phone
                if (imageUri != null) {
                    storageRef.putFile(imageUri!!)
                        .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                            GlobalScope.launch(Dispatchers.IO) {
                               taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                   profile["image"] = it.toString()
                                   println("NNNNNNNNNNNN $it")
                                   docRef.set(profile).addOnCompleteListener {
                                       Toast.makeText(this@Register, "done!", Toast.LENGTH_SHORT).show()
                                       toMain(phone)
                                   }
                               }
                            }
                        }
                } else {
                    docRef.set(profile).addOnCompleteListener {
                        Toast.makeText(this@Register, "done!", Toast.LENGTH_SHORT).show()
                        toMain(phone)
                    }
                }
            } else {
                Toast.makeText(
                    this@Register, "PLease enter your name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pick_image && data != null) {
            imageUri = data.data
            profile.setImageURI(imageUri)
            profile.isDrawingCacheEnabled = true
            profile.buildDrawingCache()
        }
    }

    private fun pickImg() {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        this.startActivityForResult(intent, 3)
    }

    private fun toMain(phone:String){
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("reg", phone)
        i.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    companion object {
        const val pick_image = 3
    }
}