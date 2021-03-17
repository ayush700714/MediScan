package com.example.mediscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {
    EditText name,doj,report_to;
    TextView already_user;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        already_user = findViewById(R.id.already_user);
        already_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jijiop121323132132", "jkjk");
                Intent intent = new Intent(SignUp.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String[] type = new String[] {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+","O-"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        SignUp.this,
                        R.layout.list_item,
                        type);

        AutoCompleteTextView editTextFilledExposedDropdown =
                findViewById(R.id.blood_group);
        editTextFilledExposedDropdown.setAdapter(adapter);

        name = findViewById(R.id.name);
        doj = findViewById(R.id.city);
        report_to = findViewById(R.id.address);

        button = findViewById(R.id.continue_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,SignUpFriend.class);
                intent.putExtra("name",name.getText().toString().trim());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("city",doj.getText().toString().trim());
                intent.putExtra("address",report_to.getText().toString().trim());
                intent.putExtra("blood_group",editTextFilledExposedDropdown.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });
    }
}