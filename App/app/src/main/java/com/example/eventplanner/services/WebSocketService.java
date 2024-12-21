package com.example.eventplanner.services;



import static android.content.Intent.getIntent;
import static java.util.jar.Pack200.Packer.ERROR;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.eventplanner.BuildConfig;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.notification.InvitationNotificationDTO;
import com.example.eventplanner.utils.NotificationSender;
import com.google.gson.Gson;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;


public class WebSocketService extends Service {

    private StompClient stompClient;
    private Disposable topicSubscription;

    private Context context;

    private static boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @SuppressLint("CheckResult")
    private void startWebSocketConnection(String email) {
        String serverUrl = "http://" + BuildConfig.IP_ADDR + ":8080/socket/websocket";

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl);

        // Handle lifecycle events
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("WebSocket", "WebSocket connection opened");
                    break;
                case ERROR:
                    Log.e("WebSocket", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    Log.d("WebSocket", "WebSocket connection closed");
                    break;
            }
        });

        // Connect to the WebSocket server
        stompClient.connect();

        // Subscribe to the topic based on the provided email
        topicSubscription = stompClient.topic("/socket-publisher/" + email)  // Topic to subscribe to
                .subscribe(topicMessage -> {
                    Log.d("WebSocket", "Received message: " + topicMessage.getPayload());

                    Context context = getApplicationContext();
                    NotificationSender.sendInvitationNotification(topicMessage, context);
                }, throwable -> {
                    Log.e("WebSocket", "Error during subscription: " + throwable.getMessage());
                });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;  // Return null as this service doesn't need to bind with any activity
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up resources when the service is destroyed
        if (stompClient != null) {
            stompClient.disconnect();
            Log.d("WebSocket", "WebSocket connection closed");
        }
        stopForeground(true);
        isServiceRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundServiceMethod();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            String email = bundle.getString("email");
            startWebSocketConnection(email);
        }
        isServiceRunning = true;
        return START_STICKY;
    }

    public static boolean isServiceRunning() {
        return isServiceRunning;
    }

    // Metoda koja pokreće foreground servis sa notifikacijom
    private void startForegroundServiceMethod() {
        NotificationChannel channel = new NotificationChannel("0", "Event invitations", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Channel for event notifications");

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.baseline_notifications_24)  // Dodajte odgovarajuću ikonu
                .setContentTitle("Notifications")
                .setContentText("You are logged in!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);  // Označite notifikaciju kao "neprekinutu"

        Notification notification = builder.build();
        startForeground(1, notification);  // Aktivirajte foreground režim odmah
    }
}