package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.eventType.GetEventTypeDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventTypeService {

    @GET("event-types/sort")
    Call<ArrayList<GetEventTypeDTO>> getAllEventTypes();

    @GET("event-types/{id}")
    Call<GetEventTypeDTO> getEventType(int id);
}
