package com.osc.randochat.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;

public class AboutUsP2Activity extends AppCompatActivity {

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us2);

        next = findViewById(R.id.btn_one2);
        AnimateView.startAnimation(R.id.bg_3 , this , 4000);
        next.setOnClickListener(view -> {
            Intent intent = new Intent(AboutUsP2Activity.this, AboutUsP3Activity.class);
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