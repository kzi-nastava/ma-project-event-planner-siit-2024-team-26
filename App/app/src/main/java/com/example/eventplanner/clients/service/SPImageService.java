package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.serviceProductImage.GetSPImageDTO;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SPImageService {
    @GET("/api/service-product-images/sp")
    Call<List<GetSPImageDTO>> getImagesByServiceProduct(
            @Query("type") String type,
            @Query("sp") int spId
    );

    @Multipart
    @POST("service-product-images/upload/{sp}")
    Call<Void> uploadImages(
            @Path("sp") int serviceProductId,
            @Query("type") String type,
            @Part List<MultipartBody.Part> files
    );
}

