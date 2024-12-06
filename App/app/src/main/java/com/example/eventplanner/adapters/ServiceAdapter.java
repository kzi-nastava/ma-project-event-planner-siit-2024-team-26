package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private List<TopServiceDTO> topServices;

    public ServiceAdapter(List<TopServiceDTO> topServices) {
        this.topServices = topServices;
    }

    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card, parent, false);
        return new ServiceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.MyViewHolder holder, int position) {
        TopServiceDTO service = topServices.get(position);
        holder.serviceName.setText(service.getName());
        holder.serviceDescription.setText(service.getDescription());
        holder.servicePrice.setText(String.valueOf(service.getPrice()) + "€");
    }


    @Override
    public int getItemCount() {
        return topServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView serviceDescription;
        TextView servicePrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);

        }
    }
}
