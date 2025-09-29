package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.dto.service.ServiceDetailsDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.model.Page;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceService {

    @GET("services/top")
    Call<ArrayList<TopServiceDTO>> getTopServices();

    @GET("services/search")
    Call<Page<ServiceCardDTO>> searchServices(
            @Query("name") String name,
            @Query("minPrice") Integer minPrice,
            @Query("maxPrice") Integer maxPrice,
            @Query("minEngagement") Integer minEngagement,
            @Query("maxEngagement") Integer maxEngagement,
            @Query("categories") List<String> categories,
            @Query("sortDirection") String sortDirection,
            @Query("size") Integer size,
            @Query("page") Integer page
    );

    @GET("services/{id}")
    Call<ServiceDetailsDTO> findService(@Path("id") Integer id);
}
