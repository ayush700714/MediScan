package com.example.mediscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.WriterException;

import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateActivity extends AppCompatActivity {

    private TextView name,phone1,age,address,blood_group,medical,friend_name1,friend_name2,friend_phone1,friend_phone2;
    private MaterialButton generate_qr;
    String medical2;
    int m=0;
    private FirebaseAuth mAuth;
    String phone;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        phone =mCurrentUser.getPhoneNumber();
        phone = phone.substring(3);

        generate_qr =findViewById(R.id.generate_qr);
        name = findViewById(R.id.name);
        phone1 = findViewById(R.id.phone);
        age = findViewById(R.id.age);
        address = findViewById(R.id.address);
        blood_group = findViewById(R.id.blood_group);
        medical= findViewById(R.id.medical_history);
        friend_name1 = findViewById(R.id.friend_name1);
        friend_name2 = findViewById(R.id.friend_name2);
        friend_phone1 = findViewById(R.id.friend_phone1);
        friend_phone2 = findViewById(R.id.friend_phone2);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        DatabaseReference current = databaseReference.child("Users").child(phone).getRef();
        current.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child:snapshot.getChildren())
                {
                    if(child.getKey().equals("name"))
                    {
                      name.setText("Name : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("age"))
                    {
                        age.setText("Age : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("address"))
                    {
                        address.setText("Address : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("blood_group"))
                    {
                        blood_group.setText("Blood Group : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("friend_name1"))
                    {
                       friend_name1.setText("Name : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("friend_name2"))
                    {
                        friend_name2.setText("Name : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("friend_phone1"))
                    {
                        friend_phone1.setText("Phone : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("friend_phone2"))
                    {
                        friend_phone2.setText("Phone : "+child.getValue().toString());
                    }
                    if(child.getKey().equals("Medical History"))
                    {
                        Log.d("1", child.getValue().toString());
                        for(DataSnapshot child2:child.getChildren())
                        {
                            Log.d("2", child2.getValue().toString());
                            for(DataSnapshot child3:child2.getChildren())
                            {
                                if (child3.getKey().equals("medical history"))
                                {
                                    Log.d("3", child3.getValue().toString());

                                    if(m==0)
                                    {
                                        medical2+=child3.getValue().toString();
                                        m=1;
                                    }
                                    else
                                    {
                                        medical2+="\n"+child3.getValue().toString();
                                    }
                                }
                            }
                        }
                    }
                }

                medical.setText(medical2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        generate_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> mp = new HashMap<>();
                mp.put("name",name.getText().toString());
                mp.put("phone",phone1.getText().toString());
                mp.put("age",age.getText().toString());
                mp.put("address",address.getText().toString());
                mp.put("blood_group",blood_group.getText().toString());
                mp.put("medical",medical.getText().toString());
                mp.put("friend_name1",friend_name1.getText().toString());
                mp.put("friend_name2",friend_name2.getText().toString());
                mp.put("friend_phone1",friend_phone1.getText().toString());
                mp.put("friend_phone2",friend_phone2.getText().toString());
                mp.put("unique_key","MEDISCAN700714");
                Gson gson = new Gson();
                String json =  gson.toJson(mp);
                Intent intent = new Intent(GenerateActivity.this,Generated.class);
                intent.putExtra("json",json);
                startActivity(intent);
            }
        });
    }
}
