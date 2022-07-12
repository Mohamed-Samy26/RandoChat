package com.osc.randochat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    FirebaseAuth mAuth;
    String mVerificationId;
    Button verify ;
    ProgressBar progressbar;
    EditText otp_code;
    String otp_from_user;
    Button send;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
   // private PhoneAuthProvider.OnVerificationStateChangedCallbacks getmCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        verify=findViewById(R.id.verifybtn);
        progressbar=findViewById(R.id.progressbar);
        otp_code=findViewById(R.id.otp_edittxt);
        send=findViewById(R.id.sendbtn);



        String phoneNo= getIntent().getStringExtra("phoneNo");  //take the number from mainactivity


          send.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  sendCode(phoneNo);

              }
          });



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(otp_code.getText().toString()))
                {
                    Toast.makeText(Register.this, "Enter valid number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                     otp_from_user = otp_code.getText().toString();
                    System.out.println(otp_from_user);
                    System.out.println(phoneNo);

                      verifycode(otp_from_user);
                }
            }
        });









    }

    private void fun(String otp_from_user) {
        otp_from_user = otp_from_user+"1";
        System.out.println("otp is "+otp_from_user);
    }


    void  sendCode(String phoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+20"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                 verifycode(code); //make sure that the code is correct

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Register.this, "Verification Failed", Toast.LENGTH_SHORT).show();
            //

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


    void verifycode(String code)
    {
       PhoneAuthCredential cradiential = PhoneAuthProvider.getCredential(mVerificationId,code);
       signinbyCradiential(cradiential);
    }

    void signinbyCradiential(PhoneAuthCredential cradiential)
    {
        FirebaseAuth firebaseauth = FirebaseAuth.getInstance();
        firebaseauth.signInWithCredential(cradiential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(Register.this,"done!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Register.this,Login.class);
                    startActivity(intent);
                }

            }
        });
    }


}
