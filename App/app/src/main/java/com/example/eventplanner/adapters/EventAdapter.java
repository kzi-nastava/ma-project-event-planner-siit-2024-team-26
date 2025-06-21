package com.example.eventplanner.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Role;
import com.example.eventplanner.utils.DateStringFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private GetAuthenticatedUserDTO currentUser;

    private List<TopEventDTO> topEvents;
    private Context context;
    private FragmentActivity fragmentActivity;
    private String reason;
    private GetChatDTO chat;

    // Used to check if user can navigate to event details tab ( if he is not blocked )

    public EventAdapter(List<TopEventDTO> events, Context context, FragmentActivity fragmentActivity, GetAuthenticatedUserDTO user) {
        this.topEvents = events;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.currentUser = user;
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
                checkIsBlockedUser(holder, event);
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

    //Checks if user can open selected event details tab
    private void checkIsBlockedUser(MyViewHolder holder, TopEventDTO event){
        if (this.currentUser != null){
            loadChat(holder, event);
        }else{
            FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, currentUser), fragmentActivity, true, R.id.mainScreenFragment);
        }
    }

    private void loadChat(MyViewHolder holder, TopEventDTO event){

        Call<GetChatDTO> call = ClientUtils.chatService.getChat(currentUser.getId(), event.getEventOrganizer().getId());
        call.enqueue(new Callback<GetChatDTO>() {

            @Override
            public void onResponse(Call<GetChatDTO> call, Response<GetChatDTO> response) {
                if (response.isSuccessful()) {
                    chat = response.body();
                    if (currentUser.getId() == chat.getEventOrganizer().getId()){
                        if (chat.isUser_1_blocked()){
                            reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                    " " + event.getEventOrganizer().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                    " " + event.getEventOrganizer().getLastName() + " is blocked!";
                            showBlockedDialog();
                        }else{
                            FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, currentUser), fragmentActivity, true, R.id.mainScreenFragment);
                        }

                    }else{ // If user is Authenticated user in chat table
                        if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                    " " + event.getEventOrganizer().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_1_blocked()) {
                            reason = "You can't see more information because organizer " + event.getEventOrganizer().getFirstName() +
                                    " " + event.getEventOrganizer().getLastName() + " is blocked!";
                            showBlockedDialog();
                        } else{
                            FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, currentUser), fragmentActivity, true, R.id.mainScreenFragment);
                        }
                    }
                }else{
                    if (response.code() == 404){
                        FragmentTransition.to(EventDetailsFragment.newInstance(event.getId(), chat, currentUser), fragmentActivity, true, R.id.mainScreenFragment);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChatDTO> call, Throwable t) {
                Log.i("ChatEventAdapter", t.getMessage());
            }
        });
    }

    private void showBlockedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);

        builder.setTitle("Access denied")
                .setMessage(reason);

        builder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
