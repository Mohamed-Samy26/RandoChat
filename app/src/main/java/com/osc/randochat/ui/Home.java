package com.osc.randochat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.osc.randochat.R;
import com.osc.randochat.chatroom.ChatRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import timber.log.Timber;


public class Home extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference roomsColl = db.collection("Rooms");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button call = findViewById(R.id.btn_call);
        EditText name = findViewById(R.id.caller);
        Query rooms = roomsColl.whereEqualTo("count" , 1);

        call.setOnClickListener(view -> {
            if(!name.getText().toString().isEmpty()) {
                rooms.get().addOnCompleteListener(task -> {
                    String s = "";
                    boolean found = false;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Timber.d( "Found documents ");
                            found = true;
                            Timber.d(document.getId() + " => " + document.getData());
                            roomsColl.document(document.getId()).update("count" , Integer.parseInt(Objects
                                    .requireNonNull(document.get("count")).toString())+1 );
                            roomsColl.document(document.getId()).update("users" ,
                                    FieldValue.arrayUnion(name.getText().toString()));
                            s = document.getId();
                            break;
                        }
                        if (!found)
                        {
                            Timber.d(task.getException(), "Error getting documents: ");
                            Map<String, Object> data = new HashMap<>();
                            data.put("count", 1);
                            data.put("users", new ArrayList<String>().add(name.getText().toString()));
                            roomsColl.document(("Room" + name.getText().toString())).set(data);
                            s = ("Room" + name.getText().toString());
                        }

                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + s);
                    openChat(name.getText().toString() ,s);
                });

            }
            else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void openChat(String name ,String s){
        Intent intent = new Intent(Home.this, ChatRoom.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", "currphone");
        intent.putExtra("RecPhone", "rp");
        intent.putExtra("image", "mm");
        intent.putExtra("room", s);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}