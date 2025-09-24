package com.example.eventplanner.dto.event;

import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.google.gson.annotations.SerializedName;

public class UserEventDTO {

    // Imena polja ("event", "user") moraju tačno odgovarati DTO klasi na beku
    @SerializedName("event")
    private GetEventDTO event;

    @SerializedName("user")
    private GetAuthenticatedUserDTO user;

    public UserEventDTO(GetEventDTO event, GetAuthenticatedUserDTO user) {
        this.event = event;
        this.user = user;
    }

    // Getteri i setteri
    public GetEventDTO getEvent() {
        return event;
    }

    public void setEvent(GetEventDTO event) {
        this.event = event;
    }

    public GetAuthenticatedUserDTO getUser() {
        return user;
    }

    public void setUser(GetAuthenticatedUserDTO user) {
        this.user = user;
    }
}