package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.dto.eventType.UpdatedEventTypeDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventTypeService {

    @GET("event-types/sort")
    Call<ArrayList<GetEventTypeDTO>> getAllEventTypes();

    @GET("event-types/{id}")
    Call<GetEventTypeDTO> getEventType(int id);

    @PUT("event-types/activation/{id}")
    Call<UpdatedEventTypeDTO> changeActivation(@Path("id") int eventTypeId);
}
