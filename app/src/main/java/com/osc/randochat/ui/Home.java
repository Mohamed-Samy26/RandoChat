package com.osc.randochat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.osc.randochat.R;
import com.osc.randochat.chatroom.ChatRoom;

public class Home extends AppCompatActivity {

    private Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        call = findViewById(R.id.btn_call);
        call.setOnClickListener(view -> {
            Intent intent = new Intent(Home.this , ChatRoom.class);
            intent.putExtra("name", "");
            intent.putExtra("phone", "");
            intent.putExtra("RecPhone", "");
            intent.putExtra("image", "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });
    }
}