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
import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

public class EventSearchAdapter extends RecyclerView.Adapter<EventSearchAdapter.MyViewHolder>{

    private List<EventCardDTO> foundEvents;
    private Context context;

    public EventSearchAdapter(List<EventCardDTO> events, Context context) {
        this.foundEvents = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card_search, parent, false);
        return new EventSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventSearchAdapter.MyViewHolder holder, int position) {
        EventCardDTO event = this.foundEvents.get(position);
        holder.eventName.setText(event.getName());
        holder.eventStarts.setText(DateStringFormatter.format(event.getStarts(), "dd.MM.yyyy. HH:mm"));
        Glide.with(this.context)
                .load(event.getImage()) // URL slike
                .into(holder.eventImage);

    }


    @Override
    public int getItemCount() {
        return this.foundEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventStarts;
        ImageView eventImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventStarts = itemView.findViewById(R.id.eventStartingDate);
            eventImage = itemView.findViewById(R.id.eventImage);

        }
    }
}
