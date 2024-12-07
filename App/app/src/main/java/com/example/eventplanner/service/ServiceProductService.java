package com.example.eventplanner.service;

import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.model.Page;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceProductService {
    @GET("services-products/search")
    Call<Page<ServiceProductCardDTO>> searchServicesAndProducts(
            @Query("name") String name,
            @Query("minPrice") Integer minPrice,
            @Query("maxPrice") Integer maxPrice,
            @Query("categories") List<String> categories,
            @Query("sortDirection") String sortDirection,
            @Query("size") Integer size,
            @Query("page") Integer page
    );
}
