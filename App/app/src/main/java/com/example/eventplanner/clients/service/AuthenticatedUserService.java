package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.CreatedAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AuthenticatedUserService {

    @GET("users/{id}")
    Call<GetAuthenticatedUserDTO> getUserByID(@Path("id") Integer id);

    @POST("users")
    Call<CreatedAuthenticatedUserDTO> createAuthenticatedUser(@Body CreateAuthenticatedUserDTO user);
}
