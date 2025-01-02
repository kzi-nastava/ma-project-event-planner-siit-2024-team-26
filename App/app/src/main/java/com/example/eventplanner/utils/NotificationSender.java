package com.example.eventplanner.utils;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.eventplanner.R;


import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.message.CreateMessageDTO;
import com.example.eventplanner.dto.message.GetMessageDTO;
import com.example.eventplanner.dto.notification.InvitationNotificationDTO;
import com.example.eventplanner.model.Role;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.dto.StompMessage;

public class NotificationSender {

    private Context context;

    private StompMessage topicMessage;

    public NotificationSender(Context context, StompMessage topicMessage) {
        this.context = context;
        this.topicMessage = topicMessage;
    }


    private boolean areNotificationsEnabled(){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this.context);
        return notificationManagerCompat.areNotificationsEnabled();
    }
    public void sendInvitationNotification(){
        if (this.areNotificationsEnabled()) {
            Gson gson = new Gson();
            InvitationNotificationDTO invitationNotificationDTO = gson.fromJson(this.topicMessage.getPayload(), InvitationNotificationDTO.class);

            NotificationChannel channel = new NotificationChannel("1", "Event invitations", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("OPIS");

            NotificationManager notificationManager = getSystemService(this.context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "1")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle(invitationNotificationDTO.getTitle())
                    .setContentText(invitationNotificationDTO.getDescription())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(1, builder.build());
            }else{
                Log.e("WebSocket", "OVDE");
            }
        }
    }

    public void sendMessageNotification(GetAuthenticatedUserDTO currentUser){
        if (this.areNotificationsEnabled()) {
            Gson gson = new Gson();
            CreateMessageDTO messageDTO = gson.fromJson(this.topicMessage.getPayload(), CreateMessageDTO.class);

            NotificationChannel channel = new NotificationChannel("2", "Message notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("OPIS");

            NotificationManager notificationManager = getSystemService(this.context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            String messageTitle;
            boolean sendNotification = false;
            String firstName = "";
            String lastName = "";
            if (currentUser.getRole() == Role.EVENT_ORGANIZER) {
                if (currentUser.getId() == messageDTO.getEventOrganizer().getId() && !messageDTO.isFromUser1()){
                    sendNotification = true;
                    firstName = messageDTO.getAuthenticatedUser().getFirstName();
                    lastName = messageDTO.getAuthenticatedUser().getLastName();
                }
            } else {
                if (currentUser.getId() == messageDTO.getAuthenticatedUser().getId() && messageDTO.isFromUser1()) {
                    sendNotification = true;
                    firstName = messageDTO.getEventOrganizer().getFirstName();
                    lastName = messageDTO.getEventOrganizer().getLastName();
                }
            }

            messageTitle = "Message from: " + firstName + " " + lastName;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context, "1")
                    .setSmallIcon(R.drawable.baseline_chat_24)
                    .setContentTitle(messageTitle)
                    .setContentText(messageDTO.getText())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            if (ContextCompat.checkSelfPermission(this.context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                if (sendNotification) {
                    notificationManager.notify(2, builder.build());
                }
            }else{
                Log.e("WebSocket", "OVDE");
            }
        }
    }
    
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public StompMessage getTopicMessage() {
        return topicMessage;
    }

    public void setTopicMessage(StompMessage topicMessage) {
        this.topicMessage = topicMessage;
    }
}
