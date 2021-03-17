package com.example.mediscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearbyHospitals extends AppCompatActivity {
    ArrayList<Hospitals> hospitals = new ArrayList<>();
    RecyclerView recyclerView;
    HostelAdapter adapter;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospitals);


        recyclerView = findViewById(R.id.recycler);
        adapter = new HostelAdapter(hospitals);


        LinearLayoutManager l = new LinearLayoutManager(NearbyHospitals.this);
        l.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);




        String url = "https://discover.search.hereapi.com";
        String location =getIntent().getStringExtra("location");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api users = retrofit.create(Api.class);

        Call<Object> call = users.getModels(location,"hospital","9yjUWaGMC-yu50RI6xgzutj4Qnlv3AbazkKPmT-zxMM",10);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                  String json = new Gson().toJson(response.body());
                Log.d("ayush", json);
                try {
                    Log.d("apspspspss", "phdfs");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    String name=null,address=null,phone=null;
                    int distance =0;
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject child = jsonArray.getJSONObject(i);
                        name = child.getString("title");
                        JSONObject address_obj = child.getJSONObject("address");
                        address = address_obj.getString("label");
                        distance = child.getInt("distance");
                        Set<String> hash_Set = new HashSet<String>();
                        Iterator<?> keys = child.keys();
                        hash_Set.clear();
                        while (keys.hasNext())
                        {
                            String key = (String)keys.next();
                            Log.d("keys", key);
                            hash_Set.add(key);
                        }
                        if(hash_Set.contains("contacts"))
                        {
                            JSONArray contacts = child.getJSONArray("contacts");
                            JSONObject one = contacts.getJSONObject(0);
                            Set<String> hash_Set2 = new HashSet<String>();
                            Iterator<?> keys2 = one.keys();
                            hash_Set2.clear();
                            while (keys2.hasNext())
                            {
                                String key = (String)keys2.next();
                                Log.d("keys", key);
                                hash_Set2.add(key);
                            }
                            if(hash_Set2.contains("phone"))
                            {
                                JSONArray phone1 = one.getJSONArray("phone");
                                JSONObject phone2 = phone1.getJSONObject(0);
                                phone = phone2.getString("value");
                            }
                            else if(hash_Set2.contains("mobile"))
                            {
                                JSONArray phone1 = one.getJSONArray("mobile");
                                JSONObject phone2 = phone1.getJSONObject(0);
                                phone = phone2.getString("value");
                            }
                            else if(hash_Set2.contains("tollFree"))
                            {
                                JSONArray phone1 = one.getJSONArray("tollFree");
                                JSONObject phone2 = phone1.getJSONObject(0);
                                phone = phone2.getString("value");
                            }

                        }
                        Log.d("information", name+address+distance+phone);
                        hospitals.add(new Hospitals(name,address,phone,distance));
                        adapter.notifyDataSetChanged();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}