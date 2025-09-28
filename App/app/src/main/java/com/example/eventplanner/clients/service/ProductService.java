package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.product.CreateProductDTO;
import com.example.eventplanner.dto.product.CreatedProductDTO;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.dto.product.ProductCardDTO;
import com.example.eventplanner.dto.product.TopProductDTO;
import com.example.eventplanner.dto.product.UpdatedProductDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.model.Page;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("products/provider")
    Call<List<GetProductDTO>> getProductsBySpp(@Query("spp_id") Integer providerId);

    // NOVA METODA: Pretraga proizvoda za provajdera
    @GET("products/provider/search")
    Call<List<GetProductDTO>> searchProductsBySpp(
            @Query("spp_id") Integer providerId,
            @Query("name") String name,
            @Query("description") String description,
            @Query("minPrice") Float minPrice,
            @Query("maxPrice") Float maxPrice,
            @Query("categories") List<Integer> categoryIds,
            @Query("available") Boolean isAvailable
    );

    @PUT("products/visibility/{id}")
    Call<UpdatedProductDTO> toggleVisibility(@Path("id") int productId);

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") int productId);

    @POST("products")
    Call<CreatedProductDTO> createProduct(@Body CreateProductDTO productDTO);
}
