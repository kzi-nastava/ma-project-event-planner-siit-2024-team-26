package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarEventsAdapter extends RecyclerView.Adapter<CalendarEventsAdapter.EventViewHolder> {

    private List<GetEventDTO> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(GetEventDTO event);
    }

    public CalendarEventsAdapter(List<GetEventDTO> events, OnEventClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_event, parent, false);
        return new EventViewHolder(view);
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private LocalDateTime toLocalDateTime(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        GetEventDTO event = events.get(position);
        holder.title.setText(event.getName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String startTime = toLocalDateTime(event.getStarts()).format(formatter);
        String endTime = toLocalDateTime(event.getEnds()).format(formatter);
        if (startTime.substring(0, 10).equals(endTime.substring(0, 10))) {
            holder.timeRange.setText(startTime.substring(11, 16) + " - " + endTime.substring(11, 16));
        } else {
            holder.timeRange.setText(startTime + " - " + endTime);
        }

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateData(List<GetEventDTO> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, timeRange;

        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            timeRange = itemView.findViewById(R.id.textTimeRange);
        }
    }
}

