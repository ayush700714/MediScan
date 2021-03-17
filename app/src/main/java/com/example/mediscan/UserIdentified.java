package com.example.mediscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class UserIdentified extends AppCompatActivity {

    private TextView name,phone1,age,address,blood_group,medical,friend_name1,friend_name2,friend_phone1,friend_phone2;
    private MaterialButton materialButton;
    private int changed=0;
    String location=null;
    LocationListener mLocationListener;
    LocationManager mLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_identified);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location locationUser) {
                if(changed==0)
                {
                    Toast.makeText(UserIdentified.this,"location changed",Toast.LENGTH_SHORT).show();
                    location = null;
                    location = String.valueOf(locationUser.getLatitude());
                    location += ",";
                    location += locationUser.getLongitude();
                    Log.d("location", location);
                    changed=1;
                }

            }
        };

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);


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
        materialButton = findViewById(R.id.hospitals);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        JSONObject jsonObject = null;
        try {
                jsonObject = new JSONObject(json);
            name.setText(jsonObject.getString("name"));
            phone1.setText(jsonObject.getString("phone"));
            age.setText(jsonObject.getString("age"));
            address.setText(jsonObject.getString("address"));
            medical.setText(jsonObject.getString("medical"));
            blood_group.setText(jsonObject.getString("blood_group"));
            friend_name1.setText(jsonObject.getString("friend_name1"));
            friend_name2.setText(jsonObject.getString("friend_name2"));
            friend_phone1.setText(jsonObject.getString("friend_phone1"));
            friend_phone2.setText(jsonObject.getString("friend_phone2"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ayush", "ayush ");
                Intent intent1 = new Intent(UserIdentified.this,NearbyHospitals.class);
                intent1.putExtra("location",location);
                startActivity(intent1);
            }
        });

    }
}