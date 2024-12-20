package com.example.eventplanner.clients.service;

import com.example.eventplanner.clients.authorization.TokenResponse;
import com.example.eventplanner.dto.authenticatedUser.LoginDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginDTO loginDTO);
}
