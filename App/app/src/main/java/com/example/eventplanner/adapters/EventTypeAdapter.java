package com.example.eventplanner.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import java.util.List;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {

    private List<GetEventTypeDTO> eventTypes;
    private final OnEventTypeActionClickListener listener;

    public interface OnEventTypeActionClickListener {
        void onInfoClicked(GetEventTypeDTO eventType);
        void onEditClicked(GetEventTypeDTO eventType);
        void onToggleStatusClicked(GetEventTypeDTO eventType);
    }

    public EventTypeAdapter(List<GetEventTypeDTO> eventTypes, OnEventTypeActionClickListener listener) {
        this.eventTypes = eventTypes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_type, parent, false);
        return new EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        holder.bind(eventTypes.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    public void updateEventTypes(List<GetEventTypeDTO> newEventTypes) {
        this.eventTypes.clear();
        if (newEventTypes != null) {
            this.eventTypes.addAll(newEventTypes);
        }
        notifyDataSetChanged();
    }

    static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        ImageButton btnInfo, btnEdit, btnToggle;
        ConstraintLayout cardLayout;

        public EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.event_type_title);
            description = itemView.findViewById(R.id.event_type_description);
            btnInfo = itemView.findViewById(R.id.btn_info);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnToggle = itemView.findViewById(R.id.btn_toggle_status);
            cardLayout = itemView.findViewById(R.id.card_content_layout);
        }

        public void bind(final GetEventTypeDTO eventType, final OnEventTypeActionClickListener listener) {
            title.setText(eventType.getName());
            description.setText(eventType.getDescription());

            boolean isActive = eventType.isActive();

            // Postavljanje boja i stanja dugmića
            btnInfo.setEnabled(isActive);
            btnEdit.setEnabled(isActive);
            btnInfo.setImageAlpha(isActive ? 255 : 130);
            btnEdit.setImageAlpha(isActive ? 255 : 130);

            if (isActive) {
                cardLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.accent)));
                btnToggle.setImageResource(R.drawable.baseline_toggle_on_24);
            } else {
                cardLayout.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.neutral)));
                btnToggle.setImageResource(R.drawable.baseline_toggle_off_24);
            }

            // Click listeneri
            btnInfo.setOnClickListener(v -> listener.onInfoClicked(eventType));
            btnEdit.setOnClickListener(v -> listener.onEditClicked(eventType));
            btnToggle.setOnClickListener(v -> listener.onToggleStatusClicked(eventType));
        }
    }
}