//Home

package com.osc.randochat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;
import com.osc.randochat.ui.about.AboutUsActivity;

public class MainActivity extends AppCompatActivity {

   LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setTheme(R.style.Theme_RandoChat_NoActionBar);
        about = findViewById(R.id.about);
        AnimateView.startAnimation(R.id.bg_home , this , 2000);
        about.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
            overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);
        });
    }

}