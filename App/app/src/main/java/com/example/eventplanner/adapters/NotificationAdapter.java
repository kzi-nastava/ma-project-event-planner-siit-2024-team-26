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
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.model.ServiceProductType;
import com.example.eventplanner.utils.DateStringFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter  extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{
    private ArrayList<GetNotificationDTO> notifications;
    private Context context;
    private View mainView;
    private ViewGroup parent;
    private boolean[] itemExpandedState;

    public NotificationAdapter(ArrayList<GetNotificationDTO> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
        this.itemExpandedState = new boolean[notifications.size()];  // Početno sve stavke su srušene
    }

    @Override
    public int getItemViewType(int position) {
        GetNotificationDTO notifaction = this.notifications.get(position);
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

        holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemExpandedState[holder.getAdapterPosition()] = !itemExpandedState[holder.getAdapterPosition()];  // Prebacujemo stanje između proširenog i skraćenog
                notifyItemChanged(holder.getAdapterPosition());
                if (!notification.isRead()){
                    notification.setRead(true);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification(notification.getId());
                notifications.remove(notification);
                notifyItemRemoved(holder.getAdapterPosition());
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

        public MyViewHolder(View itemView) {
            super(itemView);
            notificationTitle = itemView.findViewById(R.id.notificationTitle);
            notificationText = itemView.findViewById(R.id.notificationText);
            viewMoreButton = itemView.findViewById(R.id.notificationViewMoreButton);
            deleteButton = itemView.findViewById(R.id.notificationDeleteButton);
        }
    }

//    private void updateNotification(){
//        Call<GetNotificationDTO> call = ClientUtils.authenticatedUserService.getUserByEmail(email);
//        call.enqueue(new Callback<GetNotificationDTO>() {
//
//            @Override
//            public void onResponse(Call<GetNotificationDTO> call, Response<GetNotificationDTO> response) {
//                if (response.isSuccessful()) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GetNotificationDTO> call, Throwable t) {
//                Log.i("POZIV", t.getMessage());
//            }
//        });
//    }

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
}
