package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.chat.UpdateChatDTO;
import com.example.eventplanner.dto.chat.UpdatedChatDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ChatService {

    @GET("chats/{id}")
    Call<ArrayList<GetChatDTO>> getByUserId(@Path("id") Integer id);

    @PUT("chats/{eventOrganizerId}/{authenticatedUserId}")
    Call<UpdatedChatDTO> updateChat(@Body UpdateChatDTO chat, @Path("eventOrganizerId") Integer eventOrganizerId,
                                    @Path("authenticatedUserId") Integer authenticatedUserId);

    @GET("chats/{eventOrganizerId}/{authenticatedUserId}")
    Call<GetChatDTO> getChat(@Path("eventOrganizerId") Integer eventOrganizerId, @Path("authenticatedUserId") Integer authenticatedUserId);
}
