package com.example.mediscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(ScanActivity.this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String json = result.getText();
                        String key="ayush";
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            key = jsonObject.getString("unique_key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(key.equals("MEDISCAN700714"))
                        {
                            Intent intent = new Intent(ScanActivity.this,UserIdentified.class);
                            intent.putExtra("json",json);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ScanActivity.this,"Please scan a valid QR Code",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}