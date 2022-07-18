package com.osc.randochat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.osc.randochat.R;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button send;
    PinView pinView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        send=findViewById(R.id.sendbtn);
        pinView=findViewById(R.id.firstPinView);
        send.setOnClickListener(view -> {
            String code = Objects.requireNonNull(pinView.getText()).toString();
            startActivity(new Intent (Login.this,Register.class ));
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}