package com.osc.randochat.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;
import com.osc.randochat.ui.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    CircleImageView profile;
    static final int pick_image = 1;
    Uri imageUri;
    EditText user_name;
    Button sign_up_btn;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("/Images");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnimateView.startAnimation(R.id.bg_reg , this , 2000);


        profile = findViewById(R.id.profile_image);
        sign_up_btn = findViewById(R.id.signUp_btn2);
        user_name = findViewById(R.id.userName_et2);
        String phone = getIntent().getStringExtra("phone");

        if (phone.isEmpty()){
            Toast.makeText(this ,
                    "Please enter a valid number" ,Toast.LENGTH_LONG).show();
            finish();
        }
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users")
                .document(phone);


        profile.setOnClickListener(view -> {
            pickImg();
//              CropImage.startPickImageActivity(Register.this);
        });


        sign_up_btn.setOnClickListener(view -> {
            String name = user_name.getText().toString();
            if (!TextUtils.isEmpty(name))
            {
                Map<String ,String> profile = new HashMap<>();
                profile.put("name" , name);
                profile.put("phone" , phone);
                storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                        profile.put("image" , Objects.requireNonNull(taskSnapshot.getUploadSessionUri()).toString()));

                docRef.set(profile).addOnCompleteListener(task -> {
                    Toast.makeText(Register.this,"done!",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    sharedPref.edit().putString("phone" ,phone).apply();
                    Intent i = new Intent(this , MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && data != null) {
            Uri imageUri = data.getData();
            profile.setImageURI(imageUri);
            profile.setDrawingCacheEnabled(true);
            profile.buildDrawingCache();
        }
    }
    public final void pickImg() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        this.startActivityForResult(intent, 3);
    }
}




