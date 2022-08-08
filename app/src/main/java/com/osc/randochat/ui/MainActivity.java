//Home

package com.osc.randochat.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.osc.randochat.R;
import com.osc.randochat.chatroom.User;
import com.osc.randochat.helper.AnimateView;
import com.osc.randochat.ui.about.AboutUsActivity;
import com.osc.randochat.ui.fragments.JoinChat;
import com.osc.randochat.ui.fragments.Login;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    ArrayList<User> current = new ArrayList<>();
    boolean received = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setTheme(R.style.Theme_RandoChat_NoActionBar);

        LinearLayout about = findViewById(R.id.about);
        AnimateView.startAnimation(R.id.bg_home , this , 2000);
        SharedPreferences sharedPref = this.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        String phone = sharedPref.getString("phone" , null); //getIntent().getStringExtra("reg");
        CircleImageView img = findViewById(R.id.user_img);
        TextView user_name= findViewById(R.id.user_name);
        CollectionReference usersCol = FirebaseFirestore.getInstance().collection("users");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        System.out.println(">>>>>>>>>>>>>>>>>>>" + phone);
//        if (phone == null)
//            phone = sharedPref.getString("phone" ,null);
//        else
//            sharedPref.edit().putString("phone", phone).apply();
        if (phone == null){
            transaction.replace(R.id.fragment_container_view, new Login());
        }
        else{
            transaction.replace(R.id.fragment_container_view, new JoinChat());
            usersCol.document(phone).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    if( user != null) {
                        System.out.println(user.getName() + " " + user.getImage());
                        System.out.println(task.getResult().get("name"));
                        current.add(user);
                        Glide.with(this).load(user.getImage()).into(img);
                        user_name.setText(user.getName());
                        received = true;
                    }
                    else Toast.makeText(MainActivity.this, "Error getting user", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(MainActivity.this, "Error getting user", Toast.LENGTH_SHORT).show();
            });
        }
        transaction.addToBackStack(null);
        transaction.commit();
        about.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
            overridePendingTransition(org.jitsi.meet.sdk.R.anim.rns_slide_in_from_right, org.jitsi.meet.sdk.R.anim.rns_slide_out_to_left);
        });
    }
    public boolean isDataReceived(){
        return received;
    }
    public User getData(){
        return current.get(0);
    }
}