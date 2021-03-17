package com.example.mediscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup3 extends AppCompatActivity {
    String name,city,address,friend_name1,friend_name2,friend_phone1,friend_phone2,blood_group,phone;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String mAuthVerificationId;

    private EditText mOtpText;
    private Button mVerifyBtn;

    private ProgressBar mOtpProgress;

    private TextView mOtpFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        friend_name1 = intent.getStringExtra("friend_name1");
        friend_name2 = intent.getStringExtra("friend_name2");
        friend_phone1 = intent.getStringExtra("friend_phone1");
        friend_phone2 = intent.getStringExtra("friend_phone2");
        blood_group = intent.getStringExtra("blood_group");
        phone = intent.getStringExtra("phone");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");

        mOtpFeedback = findViewById(R.id.otp_form_feedback);
        mOtpProgress = findViewById(R.id.otp_progress_bar);
        mOtpText = findViewById(R.id.otp_text_view);

        mVerifyBtn = findViewById(R.id.verify_btn);

        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = mOtpText.getText().toString();

                if(otp.isEmpty()){

                    mOtpFeedback.setVisibility(View.VISIBLE);
                    mOtpFeedback.setText("Please fill in the form and try again.");

                } else {

                    mOtpProgress.setVisibility(View.VISIBLE);
                    mVerifyBtn.setEnabled(false);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Signup3.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference =firebaseDatabase.getReference().child("Employers").child(phone).getRef();
                            databaseReference.child("name").setValue(name);
                            databaseReference.child("address").setValue(city);
                            databaseReference.child("age").setValue(address);
                            databaseReference.child("friend_name1").setValue(friend_name1);
                            databaseReference.child("friend_name2").setValue(friend_name2);
                            databaseReference.child("friend_phone1").setValue(friend_phone1);
                            databaseReference.child("friend_phone2").setValue(friend_phone2);
                            databaseReference.child("blood_group").setValue(blood_group);
                            sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.d("SDFDSFDSF", task.getException().toString());
                                // The verification code entered was invalid
                                mOtpFeedback.setVisibility(View.VISIBLE);
                                mOtpFeedback.setText("There was an error verifying OTP");
                            }
                        }
                        mOtpProgress.setVisibility(View.INVISIBLE);
                        mVerifyBtn.setEnabled(true);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser != null){
            sendUserToHome();
        }
    }

    public void sendUserToHome() {
        Intent homeIntent = new Intent(Signup3.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        homeIntent.putExtra("phone",phone);
        startActivity(homeIntent);
        finish();
    }
}