package com.example.eventplanner.dto.chat;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.model.id.ChatId;

public class GetChatDTO {

    private ChatId id;
    private ChatAuthenticatedUserDTO eventOrganizer;
    private ChatAuthenticatedUserDTO authenticatedUser;
    private boolean user_1_blocked;
    private boolean user_2_blocked;

    public GetChatDTO(){super();}

    public ChatId getId() {
        return id;
    }

    public void setId(ChatId id) {
        this.id = id;
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

    public boolean isUser_1_blocked() {
        return user_1_blocked;
    }

    public void setUser_1_blocked(boolean user_1_blocked) {
        this.user_1_blocked = user_1_blocked;
    }

    public boolean isUser_2_blocked() {
        return user_2_blocked;
    }

    public void setUser_2_blocked(boolean user_2_blocked) {
        this.user_2_blocked = user_2_blocked;
    }
}
