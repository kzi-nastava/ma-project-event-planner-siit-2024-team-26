package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.chat.GetChatDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatService {

    @GET("chats/{id}")
    Call<ArrayList<GetChatDTO>> getByUserId(@Path("id") Integer id);
}
