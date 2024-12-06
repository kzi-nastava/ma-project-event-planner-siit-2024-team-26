package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.utils.DateStringFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<TopEventDTO> topEvents;

    public EventAdapter(List<TopEventDTO> events) {
        this.topEvents = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TopEventDTO event = topEvents.get(position);
        holder.eventName.setText(event.getName());
        holder.eventDescription.setText(event.getDescription());
        holder.eventStarts.setText(DateStringFormatter.format(event.getStarts(), "dd.MM.yyyy. HH:mm"));

    }


    @Override
    public int getItemCount() {
        return topEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDescription;
        TextView eventStarts;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventStarts = itemView.findViewById(R.id.eventStartingDate);

        }
    }
}
