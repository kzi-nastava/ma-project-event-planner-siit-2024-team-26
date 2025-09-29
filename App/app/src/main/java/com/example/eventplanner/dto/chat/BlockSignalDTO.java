package com.example.eventplanner.dto.chat;

import com.example.eventplanner.model.id.ChatId;

public class BlockSignalDTO {

    private ChatId chatId;
    private String blockedUserEmail;
    private boolean user1Changed;


    public BlockSignalDTO(ChatId chatId, String blockedUserEmail, boolean user1Changed) {
        this.chatId = chatId;
        this.blockedUserEmail = blockedUserEmail;
        this.user1Changed = user1Changed;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
    }

    public String getBlockedUserEmail() {
        return blockedUserEmail;
    }

    public void setBlockedUserEmail(String blockedUserEmail) {
        this.blockedUserEmail = blockedUserEmail;
    }

    public boolean isUser1Changed() {
        return user1Changed;
    }

    public void setUser1Changed(boolean user1Changed) {
        this.user1Changed = user1Changed;
    }
}

