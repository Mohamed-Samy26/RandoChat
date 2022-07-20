package com.osc.randochat.ui.about;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;

public class AboutUsActivity extends AppCompatActivity {

    Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us1);

        next_btn = findViewById(R.id.btn_one);
        AnimateView.startAnimation(R.id.bg_2 , this , 2000);
        next_btn.setOnClickListener(view -> {
            Intent intent = new Intent(AboutUsActivity.this, AboutUsP2Activity.class);
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