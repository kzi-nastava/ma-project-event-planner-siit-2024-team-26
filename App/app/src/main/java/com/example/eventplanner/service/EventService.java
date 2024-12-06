package com.example.eventplanner.service;

import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.model.Event;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventService {

    @GET("events/top")
    Call<ArrayList<TopEventDTO>> getTopEvents();
}
