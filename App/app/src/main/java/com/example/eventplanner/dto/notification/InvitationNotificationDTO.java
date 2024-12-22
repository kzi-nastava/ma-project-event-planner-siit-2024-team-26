package com.example.eventplanner.dto.notification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InvitationNotificationDTO {

    private Integer receiverId;
    private String title;
    private String description;

    public InvitationNotificationDTO(){

    }

    public InvitationNotificationDTO(Integer userId, String title, String description) {
        this.receiverId = userId;
        this.title = title;
        this.description = description;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer userId) {
        this.receiverId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
