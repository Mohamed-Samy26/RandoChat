package com.osc.randochat.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.osc.randochat.R
import com.osc.randochat.chatroom.ChatRoom
import timber.log.Timber
import java.util.*


class JoinChat : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = FirebaseFirestore.getInstance()
        val roomsColl = db.collection("Rooms")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_join_chat, container, false)
        val btn = view.findViewById<Button>(R.id.call_btn)
        val tv = view.findViewById<TextView>(R.id.call_size)
        val name = ""
        val rooms: Query = roomsColl
            .whereLessThanOrEqualTo("count", Integer.parseInt(tv.text.toString())).limit(2)

        btn.setOnClickListener {
            rooms.get().addOnCompleteListener {
                var s = ""
                if (it.isSuccessful) {
                    if (it.result.size()>0) {
                        for (document in it.result) {
                            Timber.d("Found documents ");
                            Timber.d(document.id + " => " + document.data);
                            roomsColl.document(document.id).update(
                                "count",
                                Integer.parseInt(
                                    Objects.requireNonNull(
                                        document
                                            .get("count")
                                    ).toString()
                                ) + 1
                            )
                            roomsColl.document(document.id).update(
                                "users",
                                FieldValue.arrayUnion(name)
                            )
                            s = document.id
                            break
                        }
                    }
                    else{
                        Timber.d(it.exception, "All Rooms full")
                        val data =  HashMap<String , Any>()
                        data["count"]= 1
                        data["users"] = ArrayList<String>().add(name)
                        roomsColl.document(("Room$name")).set(data)
                        s = ("Room$name")
                    }
                } else {
                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> $s");
//                    openChat(name, s);
            }
        }
    return view
  }
    fun openChat(name: String?, s: String?) {
        val intent = Intent(activity, ChatRoom::class.java)
        intent.putExtra("name", name)
//        intent.putExtra("phone", phone)
        intent.putExtra("RecPhone", "rp")
//        intent.putExtra("image", image)
        intent.putExtra("room", s)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }
}