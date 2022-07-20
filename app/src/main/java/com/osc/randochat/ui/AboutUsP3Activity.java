package com.osc.randochat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;

public class AboutUsP3Activity extends AppCompatActivity {

    Button home_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us3);

        home_btn = findViewById(R.id.home_btn);
        AnimateView.startAnimation(R.id.bg_4 , this , 4000);
        home_btn.setOnClickListener(view -> {
            Intent intent = new Intent(AboutUsP3Activity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);

        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_left, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_right);
    }

}