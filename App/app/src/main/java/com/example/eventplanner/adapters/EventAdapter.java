package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.utils.DateStringFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<TopEventDTO> topEvents;
    private Context context;

    private FragmentActivity fragmentActivity;

    public EventAdapter(List<TopEventDTO> events, Context context, FragmentActivity fragmentActivity) {
        this.topEvents = events;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
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
        Glide.with(this.context)
                .load(event.getImages().get(0)) // URL slike
                .into(holder.eventImage);

        holder.moreInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EventDetailsFragment.newInstance(event.getId()), fragmentActivity, true, R.id.mainScreenFragment);
            }
        });

    }


    @Override
    public int getItemCount() {
        return topEvents.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView eventDescription;
        TextView eventStarts;
        ImageView eventImage;

        Button moreInformationButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventName);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventStarts = itemView.findViewById(R.id.eventStartingDate);
            eventImage = itemView.findViewById(R.id.eventImage);
            moreInformationButton = itemView.findViewById(R.id.moreInformationButton);

        }
    }

}
