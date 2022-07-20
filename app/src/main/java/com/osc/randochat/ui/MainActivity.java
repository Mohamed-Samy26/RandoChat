//Home

package com.osc.randochat.ui;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;

public class MainActivity extends AppCompatActivity {

   LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        about = findViewById(R.id.about);
        AnimateView.startAnimation(R.id.bg_home , this , 4000);
        about.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
            overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);
        });
    }

}