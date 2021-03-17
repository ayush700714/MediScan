package com.example.mediscan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HostelAdapter extends RecyclerView.Adapter<HostelAdapter.RecycleHolder>{
    private Context context;
    private ArrayList<Hospitals> mEa;
    public class RecycleHolder extends RecyclerView.ViewHolder
    {
          private TextView name,phone,address,distance;
        public RecycleHolder( View itemView) {
            super(itemView);
        name = itemView.findViewById(R.id.name3);
        address = itemView.findViewById(R.id.address3);
            phone = itemView.findViewById(R.id.phone3);
            distance = itemView.findViewById(R.id.distance3);
        }
    }
    public HostelAdapter(ArrayList<Hospitals> a)
    {
        mEa=a;
    }

    @NonNull
    @Override
    public HostelAdapter.RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list,parent,false);
        RecycleHolder recycleHolder = new RecycleHolder(view);
        context=view.getContext();
        return recycleHolder;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull HostelAdapter.RecycleHolder holder, int position) {
        Hospitals b = mEa.get(position);

        holder.distance.setText("Distance :" + b.getDistance()+" metres");
        holder.address.setText("Address: " + b.getAddress());
        holder.name.setText(b.getName());
        holder.phone.setText("Phone : " +b.getPhone());

    }

    @Override
    public int getItemCount() {
        return mEa.size();
    }

}

