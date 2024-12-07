package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.MyViewHolder> {

    private List<ServiceCardDTO> foundServices;
    private Context context;

    public ServiceSearchAdapter(List<ServiceCardDTO> services, Context context) {
        this.foundServices = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card_search, parent, false);
        return new ServiceSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceSearchAdapter.MyViewHolder holder, int position) {
        ServiceCardDTO service = foundServices.get(position);
        holder.serviceName.setText(service.getName());
        holder.servicePrice.setText(String.valueOf(service.getPrice()) + "€");
        Glide.with(this.context)
                .load(service.getImage()) // URL slike
                .into(holder.serviceImage);

    }


    @Override
    public int getItemCount() {
        return this.foundServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView servicePrice;
        ImageView serviceImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceImage = itemView.findViewById(R.id.serviceImage);

        }
    }
}
