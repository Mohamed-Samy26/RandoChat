//
//package com.osc.randochat.ui;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.FirebaseException;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.PhoneAuthCredential;
//import com.google.firebase.auth.PhoneAuthOptions;
//import com.google.firebase.auth.PhoneAuthProvider;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.osc.randochat.R;
//
//import java.util.concurrent.TimeUnit;
//
//public class Home extends AppCompatActivity {
//
//    DatabaseReference ref;
//    // ConstraintLayout cons;
//    EditText phone;
//    FirebaseAuth mAuth;
//    String mVerificationId;
//    Button next;
//
//
//
//    private static final String TAG = "ReadAndWriteSnippets";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.home2);
//
//        //   cons.findViewById(R.id.next_button);
//        phone=findViewById(R.id.phone_editTxt);
//        next=findViewById(R.id.next_btn);
//
//
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(TextUtils.isEmpty(phone.getText().toString()))
//                {
//                    Toast.makeText(Home.this, "Enter valid number", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    String  phoneNo=phone.getText().toString();
//                    System.out.println("phone  "+phone.getText().toString());
//                    System.out.println("phone  "+"+20"+phoneNo);
//                    // sendCode(phone.getText().toString());
//
//                    Intent intent=new Intent(Home.this,Login.class);
//                    intent.putExtra("phoneNo",phoneNo);
//                    startActivity(intent);
//
//                }
//            }
//        });
//
//
//
//      /*  button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             /*   if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(age.getText().toString())) {
//                    addNewPost(po);
//                }
//                else
//                {
//                    Toast.makeText(MainActivity.this, "please enter data", Toast.LENGTH_SHORT).show();
//                }*/
//        //   getdata();
//               /* ref.child("users").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("firebase", "Error getting data", task.getException());
//                        }
//                        else {
//                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        }
//                    }
//                });
//            }
//        });
//*/
//
//
//
//
//
//
//    }
//    void  sendCode(String phoneNumber)
//    {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+20"+phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        //sending the code to the mobile number
//        public void onVerificationCompleted(PhoneAuthCredential credential) {
//            final String code= credential.getSmsCode(); //the code
//            if(code!=null)
//            {
//                // make sure of the code
//                verifycode(code); //make sure that the code is correct
//
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(Home.this, "Verification Failed", Toast.LENGTH_SHORT).show();
//            //
//
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s,
//                               @NonNull PhoneAuthProvider.ForceResendingToken token)
//        {
//            super.onCodeSent(s,token);
//            mVerificationId =s;
//        }
//    };
//    void verifycode(String code)
//    {
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if(currentUser != null)
//        {
//            startActivity(new Intent(Home.this,Login.class));
//            finish();
//        }
//    }
//}
