package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.category.GetCategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("categories/sort")
    Call<List<GetCategoryDTO>> getAllCategoriesAlphabetically();

    @GET("categories/{id}")
    Call<GetCategoryDTO> getCategoryById(@Path("id") int categoryId);
}
