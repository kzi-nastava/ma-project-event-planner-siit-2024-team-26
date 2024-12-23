package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.notification.GetNotificationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @GET("notifications/receiver/{id}")
    Call<ArrayList<GetNotificationDTO>> findByReceiverId(@Path("id") Integer id);

    @DELETE("notifications/{id}")
    Call<GetNotificationDTO> deleteNotification(@Path("id") Integer id);
}
