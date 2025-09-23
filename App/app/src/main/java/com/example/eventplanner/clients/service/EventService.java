package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.event.CreateEventDTO;
import com.example.eventplanner.dto.event.CreatedEventDTO;
import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Page;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventService {

    @GET("events/{id}")
    Call<GetEventDTO> getById(@Path("id") Integer id);

    @GET("events/top")
    Call<ArrayList<TopEventDTO>> getTopEvents();

    @GET("events/search")
    Call<Page<EventCardDTO>> searchEvents(
            @Query("name") String name,
            @Query("notBefore") String notBefore,
            @Query("notAfter") String notAfter,
            @Query("eventTypes") List<String> eventTypes,
            @Query("cities") List<String> cities,
            @Query("sortDirection") String sortDirection,
            @Query("size") Integer size,
            @Query("page") Integer page
    );

    @GET("events")
    Call<List<GetEventDTO>> getEventsByOrganizer(@Query("eo_id") Integer organizerId);

    @POST("events")
    Call<CreatedEventDTO> createEvent(@Body CreateEventDTO event);

    @GET("events/{eventId}/export-pdf")
    Call<byte[]> exportEventToPdf(@Query("eventId") Integer eventId);
}
