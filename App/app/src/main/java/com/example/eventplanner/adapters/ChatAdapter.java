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
import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.fragments.home_screen_fragments.SingleChatFragment;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private List<GetChatDTO> userChats;
    private Context context;
    private GetAuthenticatedUserDTO currentUser;
    private FragmentActivity fragmentActivity;

    public ChatAdapter(List<GetChatDTO> userChats, Context context, FragmentActivity fragmentActivity, GetAuthenticatedUserDTO currentUser) {
        this.userChats = userChats;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_card, parent, false);
        return new ChatAdapter.MyViewHolder(view, fragmentActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        GetChatDTO chat = userChats.get(position);

        ChatAuthenticatedUserDTO otherUser;
        boolean isAuthenticatedUser;
        if (currentUser.getId() == chat.getEventOrganizer().getId()){
            holder.userFirstAndLastName.setText(chat.getAuthenticatedUser().getFirstName() + " " + chat.getAuthenticatedUser().getLastName());
            Glide.with(this.context)
                    .load(chat.getAuthenticatedUser().getImage()) // URL slike
                    .into(holder.userImage);
            otherUser = chat.getAuthenticatedUser();
            isAuthenticatedUser = false; //  Is current user authenticateduser_idau in database table column
        }else{
            holder.userFirstAndLastName.setText(chat.getEventOrganizer().getFirstName() + " " + chat.getEventOrganizer().getLastName());
            Glide.with(this.context)
                    .load(chat.getEventOrganizer().getImage()) // URL slike
                    .into(holder.userImage);
            otherUser = chat.getEventOrganizer();
            isAuthenticatedUser = true;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("nesto", currentUser.getFirstName() + " " + otherUser.getImage());
                FragmentTransition.to(SingleChatFragment.newInstance(currentUser, otherUser, isAuthenticatedUser), fragmentActivity, true, R.id.mainScreenFragment);
            }
        });

    }


    @Override
    public int getItemCount() {
        return this.userChats.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userFirstAndLastName;
        ImageView userImage;

        public MyViewHolder(View itemView, FragmentActivity fragmentActivity) {
            super(itemView);
            userFirstAndLastName = itemView.findViewById(R.id.userFirstAndLastName);
            userImage = itemView.findViewById(R.id.userImage);

        }
    }

}
