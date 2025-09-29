package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.comment.GetCommentDTO;
import com.example.eventplanner.dto.comment.UpdateCommentDTO;
import com.example.eventplanner.dto.comment.UpdatedCommentDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CommentService {

    @GET("comments/pending")
    Call<ArrayList<GetCommentDTO>> getPendingComments();

    @PUT("comments/{id}")
    Call<UpdatedCommentDTO> updateComment(@Body UpdateCommentDTO comment, @Path("id") Integer id);
}
