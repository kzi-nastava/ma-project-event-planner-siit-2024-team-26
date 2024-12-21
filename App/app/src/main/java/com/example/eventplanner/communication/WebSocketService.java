package com.example.eventplanner.communication;


import static java.util.jar.Pack200.Packer.ERROR;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;

import android.annotation.SuppressLint;
import android.util.Log;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;


public class WebSocketService {

    private StompClient stompClient;
    private Disposable topicSubscription;

    @SuppressLint("CheckResult")
    public WebSocketService(String email) {
        // Use the correct SockJS-compatible URL for the backend WebSocket endpoint
        String serverUrl = "http://192.168.1.6:8080/socket/websocket";

        // Create the Stomp client with OkHttp connection (SockJS compatible)
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl);

        // Connecting to the WebSocket server
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

        // Establish the connection
        stompClient.connect();

        // Subscribe to a topic (channel) based on the provided email
        topicSubscription = stompClient.topic("/socket-publisher/" + email)  // Topic to subscribe to
                .subscribe(topicMessage -> {
                    Log.d("WebSocket", "Received message: " + topicMessage.getPayload());
                }, throwable -> {
                    Log.e("WebSocket", "Error during subscription: " + throwable.getMessage());
                });
    }

    // Method to disconnect
    public void disconnect() {
        // Closing the connection
        if (stompClient != null) {
            stompClient.disconnect();
            Log.d("WebSocket", "WebSocket connection closed");
        }
    }
}
