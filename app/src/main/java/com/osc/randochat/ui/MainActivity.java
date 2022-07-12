package com.osc.randochat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.osc.randochat.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

   // ConstraintLayout cons;
    EditText phone;
    FirebaseAuth mAuth;
    String mVerificationId;
    Button send;
    Button fakes;


    private static final String TAG = "ReadAndWriteSnippets";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone=findViewById(R.id.phone_editTxt);
        send=findViewById(R.id.send);
        fakes = findViewById(R.id.button);



        send.setOnClickListener(view -> {
            if(phone.getText().toString().length() != 10 )
            {
                Toast.makeText(MainActivity.this, "Enter valid number", Toast.LENGTH_SHORT).show();
            }
            else {
              String  phoneNo=phone.getText().toString();
                System.out.println("phone  "+phone.getText().toString());
                System.out.println("phone  "+"+20"+phoneNo);
                sendCode(phone.getText().toString());
            }
        });
    }


  void  sendCode(String phoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber("+20"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


   private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        //sending the code to the mobile number
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code= credential.getSmsCode(); //the code
            Intent i = new Intent();

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(MainActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
             //

        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(s,token);
            mVerificationId =s;
            startActivity(new Intent(MainActivity.this, Register.class));        }
    };
   void verifycode(String code)
   {

   }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null)
        {
//            startActivity(new Intent(MainActivity.this,Login.class));
//            finish();
        }
    }
}
