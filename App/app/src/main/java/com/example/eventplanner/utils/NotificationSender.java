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
import com.example.eventplanner.dto.notification.InvitationNotificationDTO;
import com.google.gson.Gson;

import ua.naiksoftware.stomp.dto.StompMessage;

public class NotificationSender {

    private static boolean areNotificationsEnabled(Context context){
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        return notificationManagerCompat.areNotificationsEnabled();
    }
    public static void sendInvitationNotification(StompMessage topicMessage, Context context){
        if (areNotificationsEnabled(context)) {
            Gson gson = new Gson();
            InvitationNotificationDTO invitationNotificationDTO = gson.fromJson(topicMessage.getPayload(), InvitationNotificationDTO.class);

            NotificationChannel channel = new NotificationChannel("1", "Event invitations", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("OPIS");

            NotificationManager notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle(invitationNotificationDTO.getTitle())
                    .setContentText(invitationNotificationDTO.getDescription())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(0, builder.build());
            }else{
                Log.e("WebSocket", "OVDE");
            }
        }
    }


}
