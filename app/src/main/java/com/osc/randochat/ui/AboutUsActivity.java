package com.osc.randochat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.osc.randochat.R;

public class AboutUsActivity extends AppCompatActivity {

    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us1);

        next_btn = findViewById(R.id.btn_one);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, AboutUsP2Activity.class);
                startActivity(intent);
                overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_left, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_right);
    }

}