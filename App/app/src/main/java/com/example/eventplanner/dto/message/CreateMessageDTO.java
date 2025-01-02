package com.example.eventplanner.dto.message;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;

import java.time.LocalDateTime;

public class CreateMessageDTO {

    private ChatAuthenticatedUserDTO eventOrganizer;
    private ChatAuthenticatedUserDTO authenticatedUser;
    private String text;
    private boolean fromUser1;
    private LocalDateTime timeStamp;

    public CreateMessageDTO(){super();}

    public CreateMessageDTO(ChatAuthenticatedUserDTO eventOrganizer, ChatAuthenticatedUserDTO authenticatedUser, String text, boolean fromUser1, LocalDateTime timeStamp) {
        this.eventOrganizer = eventOrganizer;
        this.authenticatedUser = authenticatedUser;
        this.text = text;
        this.fromUser1 = fromUser1;
        this.timeStamp = timeStamp;
    }

    public ChatAuthenticatedUserDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(ChatAuthenticatedUserDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public ChatAuthenticatedUserDTO getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(ChatAuthenticatedUserDTO authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromUser1() {
        return fromUser1;
    }

    public void setFromUser1(boolean fromUser1) {
        this.fromUser1 = fromUser1;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
