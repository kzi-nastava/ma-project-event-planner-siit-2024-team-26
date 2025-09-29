package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.event.UserEventDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GoingToService {
    @POST("is-going")
    Call<UserEventDTO> addGoingToEvent(@Body UserEventDTO dto);

    @DELETE("is-going/{eventId}/{userId}")
    Call<Void> removeGoingToEvent(@Path("eventId") Integer eventId, @Path("userId") Integer userId);

}
