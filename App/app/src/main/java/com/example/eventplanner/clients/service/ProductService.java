package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.dto.product.ProductCardDTO;
import com.example.eventplanner.dto.product.TopProductDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.model.Page;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {

    @GET("products/{id}")
    Call<GetProductDTO> getById(@Path("id") Integer id);

    @GET("products/top")
    Call<ArrayList<TopProductDTO>> getTopProducts();

    @GET("products/search")
    Call<Page<ProductCardDTO>> searchProducts(
            @Query("name") String name,
            @Query("minPrice") Integer minPrice,
            @Query("maxPrice") Integer maxPrice,
            @Query("categories") List<String> categories,
            @Query("sortDirection") String sortDirection,
            @Query("size") Integer size,
            @Query("page") Integer page
    );
}
