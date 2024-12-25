package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.invitation.CreateInvitationDTO;
import com.example.eventplanner.dto.invitation.CreatedInvitationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InvitationService {

    @POST("invitations")
    Call<CreatedInvitationDTO> createInvitation(@Body CreateInvitationDTO invitationDTO);
}
