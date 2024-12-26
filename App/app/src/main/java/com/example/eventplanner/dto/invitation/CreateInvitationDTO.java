package com.example.eventplanner.dto.invitation;

import com.example.eventplanner.dto.event.EventReservationDTO;

public class CreateInvitationDTO {
    private String email;
    private EventReservationDTO event;
    private String text;


    public CreateInvitationDTO(){super();}

    public CreateInvitationDTO(String email, EventReservationDTO event, String text) {
        this.email = email;
        this.event = event;
        this.text = text;
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
