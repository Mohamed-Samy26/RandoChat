//Home

package com.osc.randochat.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;
import com.osc.randochat.ui.about.AboutUsActivity;
import com.osc.randochat.ui.fragments.JoinChat;
import com.osc.randochat.ui.fragments.Login;

public class MainActivity extends AppCompatActivity {

   LinearLayout about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setTheme(R.style.Theme_RandoChat_NoActionBar);
        about = findViewById(R.id.about);
        FragmentContainerView fragmentContainerView = findViewById(R.id.fragment_container_view);
        AnimateView.startAnimation(R.id.bg_home , this , 2000);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String phone = sharedPref.getString("phone" ,"");
        if (phone.isEmpty()){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view, new Login());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container_view, new JoinChat());
            transaction.addToBackStack(null);
            transaction.commit();
        }
        about.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
            overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);
        });
    }

}