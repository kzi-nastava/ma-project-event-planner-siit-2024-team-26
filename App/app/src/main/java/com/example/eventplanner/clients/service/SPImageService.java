package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.serviceProductImage.GetSPImageDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SPImageService {
    @GET("/api/service-product-images/sp")
    Call<List<GetSPImageDTO>> getImagesByServiceProduct(
            @Query("type") String type,
            @Query("sp") int spId
    );
}

