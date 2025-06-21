package com.example.eventplanner.dto.chat;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.model.id.ChatId;

public class CreatedChatDTO {

    private ChatId chatId;
    private ChatAuthenticatedUserDTO eventOrganizer;
    private ChatAuthenticatedUserDTO authenticatedUser;
    private boolean user1Blocked;
    private boolean user2Blocked;

    public CreatedChatDTO(ChatId chatId, ChatAuthenticatedUserDTO eventOrganizer, ChatAuthenticatedUserDTO authenticatedUser, boolean user1Blocked, boolean user2Blocked) {
        this.chatId = chatId;
        this.eventOrganizer = eventOrganizer;
        this.authenticatedUser = authenticatedUser;
        this.user1Blocked = user1Blocked;
        this.user2Blocked = user2Blocked;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
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

    public boolean isUser1Blocked() {
        return user1Blocked;
    }

    public void setUser1Blocked(boolean user1Blocked) {
        this.user1Blocked = user1Blocked;
    }

    public boolean isUser2Blocked() {
        return user2Blocked;
    }

    public void setUser2Blocked(boolean user2Blocked) {
        this.user2Blocked = user2Blocked;
    }
}
