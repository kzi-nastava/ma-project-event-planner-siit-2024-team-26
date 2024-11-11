package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
        Event event = events.get(position);
        holder.eventName.setText("Name: "+event.getName());
        holder.eventType.setText("Type: "+event.getEventType().getName());
        holder.eventGuestsLimit.setText("Guests limit: "+String.valueOf(event.getGuestsLimit()));

        String startingDate = sdf.format(event.getStarts().getTime());
        holder.eventStarts.setText("Starts: "+startingDate);

        String endingDate = sdf.format(event.getEnds().getTime());
        holder.eventEnds.setText("Ends: "+endingDate);
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventType;
        TextView eventGuestsLimit;
        TextView eventStarts;
        TextView eventEnds;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventType = itemView.findViewById(R.id.eventTypeName);
            eventGuestsLimit = itemView.findViewById(R.id.eventGuestsLimit);
            eventStarts = itemView.findViewById(R.id.eventStartingDate);
            eventEnds = itemView.findViewById(R.id.eventEndingDate);
        }
    }
}
