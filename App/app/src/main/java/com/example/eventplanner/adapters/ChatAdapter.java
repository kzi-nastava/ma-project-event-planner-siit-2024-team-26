package com.example.eventplanner.adapters;

import android.content.Context;
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
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<GetChatDTO> userChats;
    private Context context;

    private FragmentActivity fragmentActivity;

    public ChatAdapter(List<GetChatDTO> userChats, Context context, FragmentActivity fragmentActivity) {
        this.userChats = userChats;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);
        return new ChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        GetChatDTO chat = userChats.get(position);

//        Glide.with(this.context)
//                .load(cha.getImages().get(0)) // URL slike
//                .into(holder.eventImage);

    }


    @Override
    public int getItemCount() {
        return this.userChats.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);


        }
    }
}
