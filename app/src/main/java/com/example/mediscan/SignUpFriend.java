package com.example.mediscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class SignUpFriend extends AppCompatActivity {
    private TextInputEditText name1,name2,phone1,phone2;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_friend);

        name1 = findViewById(R.id.name1);
        phone1 = findViewById(R.id.phone1);
        name2 = findViewById(R.id.name2);
        phone2 = findViewById(R.id.phone2);

        button = findViewById(R.id.continue_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpFriend.this,SignUp2.class);
                intent.putExtra("friend_name1",name1.getText().toString().trim());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("friend_name2",name2.getText().toString().trim());
                intent.putExtra("friend_phone1",phone1.getText().toString().trim());
                intent.putExtra("friend_phone2",phone2.getText().toString().trim());
                intent.putExtra("name",getIntent().getStringExtra("name"));
                intent.putExtra("city",getIntent().getStringExtra("city"));
                intent.putExtra("address",getIntent().getStringExtra("address"));
                intent.putExtra("blood_group",getIntent().getStringExtra("blood_group"));
                startActivity(intent);
                finish();
            }
        });
    }
}