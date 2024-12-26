package com.example.eventplanner.dto.invitation;

import com.example.eventplanner.dto.event.EventReservationDTO;

public class CreatedInvitationDTO {
    private Integer id;
    private String email;
    private EventReservationDTO event;
    private String text;

    public CreatedInvitationDTO(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EventReservationDTO getEvent() {
        return event;
    }

    public void setEvent(EventReservationDTO event) {
        this.event = event;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
