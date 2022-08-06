package com.osc.randochat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.osc.randochat.R
import com.osc.randochat.chatroom.ChatRoom
import com.osc.randochat.chatroom.User
import com.osc.randochat.ui.MainActivity
import com.osc.randochat.ui.login.Verify
import timber.log.Timber
import java.util.*


class Login : Fragment(R.layout.fragment_login) {

    val user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val roomsColl = FirebaseFirestore.getInstance().collection("rooms");
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(
            R.layout.fragment_login,
            container, false
        )
        val btn = view.findViewById<Button>(R.id.enter_btn)
        val txt = view.findViewById<TextInputEditText>(R.id.enter_phone)
        val rooms: Query = roomsColl.whereEqualTo("count", 1)

        btn.setOnClickListener {
            if (txt.text.toString().length >= 7){
                val intent = Intent(activity , Verify::class.java)
                intent.putExtra("phone" , txt.text.toString())
                startActivity(intent)
            }
        }
        val home: MainActivity = activity as MainActivity
        btn.setOnClickListener {
            if (home.isDataReceived) {
                val user = home.data
                val name = user.name
                rooms.get().addOnCompleteListener {
                    var s = ""
                    var found = false;
                    if (it.isSuccessful) {
                        for ( document in it.result ) {
                            Timber.d("Found documents ");
                            found = true;
                            Timber.d(document.id + " => " + document.data);
                            roomsColl.document(document.id).update(
                                "count", Integer.parseInt(
                                    Objects
                                        .requireNonNull(document.get("count")).toString()
                                ) + 1
                            );
                            roomsColl.document(document.id).update(
                                "users",
                                FieldValue.arrayUnion(name)
                            );
                            s = document.id;
                            break;
                        }
                        if (!found) {
                            Timber.d(it.exception, "Error getting documents: ");
                            val data =  HashMap<String , Any>();
                            data["count"] = 1;
                            data["users"] = ArrayList<String>().add(name);
                            roomsColl.document(("Room$name")).set(data);
                            s = ("Room$name");
                        }

                    } else {
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    openChat(s);
                }

            } else {
                Toast.makeText(activity, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        }
        return view
    }
    private fun openChat(s: String?) {
        if (user != null) {
            val intent = Intent(activity, ChatRoom::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("phone", user.phone)
            intent.putExtra("RecPhone", "rp")
            intent.putExtra("image", user.image)
            intent.putExtra("room", s)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
        }
    }
}