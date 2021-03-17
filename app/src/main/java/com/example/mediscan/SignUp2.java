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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class SignUp2 extends AppCompatActivity {


    int flag=0;
    String complete_phone_number,phone_number;
    String name,city,address,friend_name1,friend_name2,friend_phone1,friend_phone2,blood_group;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private EditText mCountryCode;
    private EditText mPhoneNumber;

    private Button mGenerateBtn;
    private ProgressBar mLoginProgress;

    private TextView mLoginFeedbackText;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        city = intent.getStringExtra("city");
        address = intent.getStringExtra("address");
        friend_name1 = intent.getStringExtra("friend_name1");
        friend_name2 = intent.getStringExtra("friend_name2");
        friend_phone1 = intent.getStringExtra("friend_phone1");
        friend_phone2 = intent.getStringExtra("friend_phone2");
        blood_group = intent.getStringExtra("blood_group");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mCountryCode = findViewById(R.id.country_code_text);
        mPhoneNumber = findViewById(R.id.phone_number_text);
        mGenerateBtn = findViewById(R.id.generate_btn);
        mLoginProgress = findViewById(R.id.login_progress_bar);
        mLoginFeedbackText = findViewById(R.id.login_form_feedback);

        mGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = mCountryCode.getText().toString();
                phone_number = mPhoneNumber.getText().toString();

                complete_phone_number = "+" + country_code + phone_number;

                if(country_code.isEmpty() || phone_number.isEmpty()){
                    mLoginFeedbackText.setText("Please fill in the form to continue.");
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                } else {
                    mLoginProgress.setVisibility(View.VISIBLE);
                    mGenerateBtn.setEnabled(false);
                    check_NotExist();

                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }



            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.d("SDFSDFerror message", e.toString());
                mLoginFeedbackText.setText("Verification Failed, please try again.");
                mLoginFeedbackText.setVisibility(View.VISIBLE);
                mLoginProgress.setVisibility(View.INVISIBLE);
                mGenerateBtn.setEnabled(true);
            }

            @Override
            public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent otpIntent = new Intent(SignUp2.this, Signup3.class);
                                otpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                otpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                otpIntent.putExtra("phone",mPhoneNumber.getText().toString().trim());
                                otpIntent.putExtra("name",name);
                                otpIntent.putExtra("address",city);
                                otpIntent.putExtra("age",address);
                                otpIntent.putExtra("AuthCredentials", s);
                                otpIntent.putExtra("friend_name1",friend_name1);
                                otpIntent.putExtra("friend_name2",friend_name2);
                                otpIntent.putExtra("friend_phone1",friend_phone1);
                                otpIntent.putExtra("friend_phone2",friend_phone2);
                                otpIntent.putExtra("blood_group",blood_group);
                                startActivity(otpIntent);
                            }
                        },
                        10000);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser != null){
            sendUserToHome();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUp2.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference =firebaseDatabase.getReference().child("Users").child(mPhoneNumber.getText().toString().trim()).getRef();
                            databaseReference.child("name").setValue(name);
                            databaseReference.child("address").setValue(city);
                            databaseReference.child("age").setValue(address);
                            databaseReference.child("friend_name1").setValue(friend_name1);
                            databaseReference.child("friend_name2").setValue(friend_name2);
                            databaseReference.child("friend_phone1").setValue(friend_phone1);
                            databaseReference.child("friend_phone2").setValue(friend_phone2);
                            databaseReference.child("blood_group").setValue(blood_group);
                            sendUserToHome();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText("There was an error verifying OTP");
                            }
                        }
                        mLoginProgress.setVisibility(View.INVISIBLE);
                        mGenerateBtn.setEnabled(true);
                    }
                });
    }

    private void sendUserToHome() {
        Intent homeIntent = new Intent(SignUp2.this, MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        homeIntent.putExtra("phone",mPhoneNumber.getText().toString().trim());
        startActivity(homeIntent);
        finish();
    }

    public void check_NotExist()
    {
        Log.d("checkNot","CheckNot");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(phone_number).getRef();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    flag=1;
                }
                send_msg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void send_msg()
    {
        Log.d("send_msg", "SDF  " + flag+"  send_msg");
        if(flag==0)
        {



            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    complete_phone_number,
                    60,
                    TimeUnit.SECONDS,
                    SignUp2.this,
                    mCallbacks
            );
        }
        else
        {
            mLoginProgress.setVisibility(View.INVISIBLE);
            mGenerateBtn.setEnabled(true);
            mLoginFeedbackText.setVisibility(View.VISIBLE);
            mLoginFeedbackText.setText("User Already Exist");
        }
    }
}