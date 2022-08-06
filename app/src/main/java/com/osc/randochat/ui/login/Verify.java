package com.osc.randochat.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.osc.randochat.R;
import com.osc.randochat.helper.AnimateView;
import com.osc.randochat.ui.MainActivity;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class Verify extends AppCompatActivity {

    private static final String TAG = "tag";
    FirebaseFirestore db;
    String mVerificationId;
    Button verify ;
    ProgressBar progressbar;
    EditText otp_code;
    String otp_from_user;
    String phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        AnimateView.startAnimation(R.id.bg_verify , this , 2000);
        verify=findViewById(R.id.verifybtn);
        progressbar=findViewById(R.id.progressbar);
        otp_code=findViewById(R.id.otp_edittxt);
        db = FirebaseFirestore.getInstance();

        final String phone = "+20" +getIntent().getStringExtra("phone");
        phoneNo=phone;
        if (phone.length()<=3){
            Toast.makeText(this ,
                "Please enter a valid number "+ phone ,Toast.LENGTH_LONG).show();
            finish();
        }
        sendCode(phone);


        verify.setOnClickListener(view -> {
            if(TextUtils.isEmpty(otp_code.getText().toString()))
            {
                Toast.makeText(Verify.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
            }
            else
            {
                otp_from_user = otp_code.getText().toString();
                System.out.println(otp_from_user);
                System.out.println(phone);
                verifyCode(otp_from_user);
            }
        });
    }

     PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        //sending the code to the mobile number
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code= credential.getSmsCode(); //the code
            if(code!=null)
            {
                progressbar.setVisibility(View.VISIBLE);
                verifyCode(code); //make sure that the code is correct
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Verify.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        //special case
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(s,token);
            mVerificationId =s;
        }
    };


    void verifyCode(String code)
    {
       PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,code);
       signingCredential(credential);
    }

    void signingCredential(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                Toast.makeText(Verify.this,"done!",Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = this.getSharedPreferences("prefs",Context.MODE_PRIVATE);
                sharedPref.edit().putString("phone" ,phoneNo).apply();
                checkRegistered(phoneNo);
            }
        });
    }
    void  sendCode(String phoneNumber)
    {
        System.out.println("sending code TO "+ phoneNumber);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    void checkRegistered(String phoneNo){
        System.out.println("checking >>>>>>>>>>>>>>>> "+phoneNo);
        final Task<DocumentSnapshot> res = db.collection("users").document(""+phoneNo)
                .get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Timber.tag(TAG).d("Found");
                            Intent i = new Intent(this , MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                        else {
                            Timber.tag(TAG).d("Not Found");
                            Intent i = new Intent(this , Register.class);
                            i.putExtra("phone" , phoneNo);
                            startActivity(i);
                        }
                    } else {
                        Timber.tag(TAG).d(task.getException(), "Failed with: ");
                    }
                }
        );
    }
}
