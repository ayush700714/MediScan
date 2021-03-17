package com.example.mediscan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION = 1;
    String mPermissionLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    String mPermissionCamera = Manifest.permission.CAMERA;
    String mPermissionCoarse = Manifest.permission.ACCESS_COARSE_LOCATION;


    private RelativeLayout issues,account;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    String phone;
    private TextInputEditText medical_history;
    private MaterialButton medical_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       getCameraPermission();



        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();



        medical_btn = findViewById(R.id.add_medical);
        medical_history = findViewById(R.id.medical_history);
        medical_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone =         mCurrentUser.getPhoneNumber();
                phone = phone.substring(3);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference current = firebaseDatabase.getReference().child("Users").child(phone).child("Medical History");
                String medical_history2 = medical_history.getText().toString().trim();
                current.push().child("medical history").setValue(medical_history2);
                medical_history.setText("");
                Toast.makeText(MainActivity.this,"Added Successfully",Toast.LENGTH_SHORT).show();
            }
        });


        MaterialCardView materialCardView3 = findViewById(R.id.card3);
        materialCardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView materialCardView1 = findViewById(R.id.card1);
        materialCardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });

        MaterialCardView materialCardView2 = findViewById(R.id.card2);
        materialCardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mCurrentUser == null){
            sendUserToLogin();
        }
    }

    private void sendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    public void getCameraPermission()
    {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M) {

            if (checkSelfPermission(mPermissionCamera) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(mPermissionLocation) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(mPermissionCoarse) != PackageManager.PERMISSION_GRANTED) {
                Log.d("check", "getCameraPermission:");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{mPermissionCamera,mPermissionLocation,mPermissionCoarse}, REQUEST_CODE_LOCATION);
            }

        }
    }




     @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         if (requestCode == REQUEST_CODE_LOCATION) {
             if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 Toast.makeText(MainActivity.this,"given",Toast.LENGTH_SHORT).show();
             }
             else{
                 Toast.makeText(MainActivity.this,"Please provide all permissions",Toast.LENGTH_SHORT).show();
                 this.getCameraPermission();
             }
         }
    }
}