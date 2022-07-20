package com.osc.randochat.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    CircleImageView profile;
    static final int pick_image = 1;
    Uri imageUri;
    String path;
    EditText user_name;
    EditText phone_number;
    TextView signIn_tv;
    Button sign_up_btn;
    DocumentReference docRef = FirebaseFirestore.getInstance().collection("users")
            .document();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnimateView.startAnimation(R.id.bg_reg , this , 2000);


        profile = findViewById(R.id.profile_image);
        sign_up_btn = findViewById(R.id.signUp_btn2);
        user_name = findViewById(R.id.userName_et2);


        profile.setOnClickListener(view -> {
            pickImg();
//              CropImage.startPickImageActivity(Register.this);
        });


        sign_up_btn.setOnClickListener(view -> {

             if (!TextUtils.isEmpty(user_name.getText().toString()))
            {
                String name = user_name.getText().toString();
                System.out.println("name is " + name);
            }
             else
            {
                Toast.makeText(Register.this, "PLease enter your name",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    void uploadImage(Uri uri)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && data != null) {
            Uri imageUri = data.getData();
            profile.setImageURI(imageUri);
        }
    }
    public final void pickImg() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        this.startActivityForResult(intent, 3);
    }
}




