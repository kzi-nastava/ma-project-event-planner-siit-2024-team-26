package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.notification.GetNotificationDTO;
import com.example.eventplanner.dto.notification.UpdateNotificationDTO;
import com.example.eventplanner.dto.notification.UpdatedNotificationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @GET("notifications/receiver/{id}")
    Call<ArrayList<GetNotificationDTO>> findByReceiverId(@Path("id") Integer id);

    @DELETE("notifications/{id}")
    Call<GetNotificationDTO> deleteNotification(@Path("id") Integer id);

    @PUT("notifications/{id}")
    Call<UpdatedNotificationDTO> updateNotification(@Body UpdateNotificationDTO notification, @Path("id") Integer id);
}
