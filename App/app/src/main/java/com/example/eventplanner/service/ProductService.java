package com.example.eventplanner.service;

import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.product.TopProductDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductService {

    @GET("products/top")
    Call<ArrayList<TopProductDTO>> getTopProducts();
}
