package com.osc.randochat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.osc.randochat.R;

public class Home extends AppCompatActivity {

    private Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        call = findViewById(R.id.btn_call);
        call.setOnClickListener(view -> {

        });
    }
}