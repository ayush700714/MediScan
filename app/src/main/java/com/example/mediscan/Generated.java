package com.example.mediscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Generated extends AppCompatActivity {
    private ImageView qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated);
        Intent intent = getIntent();
        Log.d("json",intent.getStringExtra("json"));
        String json = intent.getStringExtra("json");

        qr_code =findViewById(R.id.qr_code);

        QRGEncoder qrgEncoder = new QRGEncoder(json, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap bitmap;
            bitmap = qrgEncoder.getBitmap();
            qr_code.setImageBitmap(bitmap);
        } finally {
            Toast.makeText(Generated.this,"success",Toast.LENGTH_SHORT).show();
        }
    }
}