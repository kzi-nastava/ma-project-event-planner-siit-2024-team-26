package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.message.GetMessageDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MessageService {

    @GET("messages/{user1Id}/{user2Id}")
    Call<ArrayList<GetMessageDTO>> getByUsers(@Path("user1Id") Integer user1Id, @Path("user2Id") Integer user2Id);
}
