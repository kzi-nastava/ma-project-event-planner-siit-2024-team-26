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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.notification.GetNotificationDTO;
import com.example.eventplanner.dto.notification.UpdateNotificationDTO;
import com.example.eventplanner.dto.notification.UpdatedNotificationDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.model.ServiceProductType;
import com.example.eventplanner.utils.DateStringFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    private ArrayList<GetNotificationDTO> notifications;
    private Context context;
    private View mainView;
    private ViewGroup parent;
    private boolean[] itemExpandedState;
    private TextView notificationNumberTextView;

    private ArrayList<Integer> unreadIndexes;


    public NotificationAdapter(ArrayList<GetNotificationDTO> notifications, Context context, TextView notificationNumberTextView) {
        this.notifications = notifications;
        this.context = context;
        this.itemExpandedState = new boolean[notifications.size()];  // Početno sve stavke su srušene
        this.notificationNumberTextView = notificationNumberTextView;
        unreadIndexes = new ArrayList<>();
        setNotificationNumberText();
        setUnreadIndexes();
    }

    @Override
    public int getItemViewType(int position) {
        GetNotificationDTO notifaction = this.notifications.get(position);

        String x = "";
        for (int i : unreadIndexes){
            x += String.valueOf(i) + " ";
        }
        Log.i("index", x);

        if (unreadIndexes.contains(position)){
            return 1;
        }
        if (!itemExpandedState[position]){
            if (notifaction.isRead()){
                return 3;
            }
            return 1;
        }else{
            return 2;
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
                return new NotificationAdapter.MyViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_expanded, parent, false);
                return new NotificationAdapter.MyViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card_read, parent, false);
                return new NotificationAdapter.MyViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        GetNotificationDTO notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationText.setText(notification.getText());
        holder.notificationTimeStamp.setText(DateStringFormatter.format(notification.getTimeStamp(), "dd.MM.yyyy. HH:mm"));

        holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemExpandedState[holder.getAdapterPosition()] = !itemExpandedState[holder.getAdapterPosition()];  // Prebacujemo stanje između proširenog i skraćenog
                if (!notification.isRead()){
                    notification.setRead(true);
                    removeFromUdnreadIndexes(holder.getAdapterPosition());
                    updateNotification(notification);
                    setNotificationNumberText();
                }
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isRead()){
                    if (itemExpandedState[holder.getAdapterPosition()]) {
                        itemExpandedState[holder.getAdapterPosition()] = !itemExpandedState[holder.getAdapterPosition()];
                    }
                }


                updateUnreadIndexes(holder.getAdapterPosition());
                deleteNotification(notification.getId());
                notifyItemRemoved(holder.getAdapterPosition());
                notifications.remove(notification);
                setNotificationNumberText();
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.notifications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notificationTitle;
        TextView notificationText;

        Button viewMoreButton;
        Button deleteButton;

        TextView notificationTimeStamp;

        public MyViewHolder(View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationText = itemView.findViewById(R.id.notificationText);
            viewMoreButton = itemView.findViewById(R.id.notificationViewMoreButton);
            deleteButton = itemView.findViewById(R.id.notificationDeleteButton);
            notificationTimeStamp = itemView.findViewById(R.id.notificationTimeStamp);
        }
    }

    private void updateNotification(GetNotificationDTO notification){
        UpdateNotificationDTO updateNotificationDTO = new UpdateNotificationDTO(notification);
        Log.i("POZIV", String.valueOf(updateNotificationDTO.isRead()));
        Call<UpdatedNotificationDTO> call = ClientUtils.notificationService.updateNotification(updateNotificationDTO, notification.getId());
        call.enqueue(new Callback<UpdatedNotificationDTO>() {

            @Override
            public void onResponse(Call<UpdatedNotificationDTO> call, Response<UpdatedNotificationDTO> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<UpdatedNotificationDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void deleteNotification(Integer id){
        Call<GetNotificationDTO> call = ClientUtils.notificationService.deleteNotification(id);
        call.enqueue(new Callback<GetNotificationDTO>() {

            @Override
            public void onResponse(Call<GetNotificationDTO> call, Response<GetNotificationDTO> response) {
            }

            @Override
            public void onFailure(Call<GetNotificationDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setNotificationNumberText(){
        int numUnreadNotifications = 0;
        for (GetNotificationDTO notificationDTO : notifications){
            if (!notificationDTO.isRead()){
                numUnreadNotifications += 1;
            }
        }
        if (notifications.size() == 0){
            notificationNumberTextView.setText("You have no notifications!");
        }else if (numUnreadNotifications == 0){
            notificationNumberTextView.setText("You have read all notifications!");
        }else if (numUnreadNotifications == 1){
            notificationNumberTextView.setText("You have " + numUnreadNotifications + " unread notification!");
        }
        else{
            notificationNumberTextView.setText("You have " + numUnreadNotifications + " unread notifications!");
        }

    }

    private void setUnreadIndexes(){
        for (int i = 0; i < notifications.size(); i++){
            if (!notifications.get(i).isRead()){
                unreadIndexes.add(i);
            }
        }
    }

    private void updateUnreadIndexes(int index){
        removeFromUdnreadIndexes(index);
        int listIndex = -1;
        for (int i : unreadIndexes) {
            listIndex += 1;
            if (i > index) {
                unreadIndexes.set(listIndex, i - 1);
            }
        }
    }

    private void removeFromUdnreadIndexes(int index){
        if (unreadIndexes.contains(index)){
            int x = unreadIndexes.indexOf(index);
            unreadIndexes.remove(x);
        }
    }
}
