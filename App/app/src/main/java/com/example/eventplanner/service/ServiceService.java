package com.example.eventplanner.service;

import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServiceService {

    @GET("services/top")
    Call<ArrayList<TopServiceDTO>> getTopServices();
}
