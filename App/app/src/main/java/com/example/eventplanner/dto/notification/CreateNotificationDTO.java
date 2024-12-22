package com.example.eventplanner.dto.notification;

import java.time.LocalDateTime;

public class CreateNotificationDTO {
    private Integer receiverId;
    private String title;
    private String text;
    private LocalDateTime timeStamp;
    private boolean isRead;
    private boolean isDeleted;

    public CreateNotificationDTO(){super();}

    public CreateNotificationDTO(Integer receiverId, String title, String text, LocalDateTime timeStamp, boolean isRead, boolean isDeleted) {
        this.receiverId = receiverId;
        this.title = title;
        this.text = text;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
    }

    public CreateNotificationDTO(InvitationNotificationDTO invitationNotificationDTO){
        this.receiverId = invitationNotificationDTO.getReceiverId();
        this.title = invitationNotificationDTO.getTitle();
        this.text = invitationNotificationDTO.getDescription();
        this.timeStamp = LocalDateTime.now();
        this.isRead = false;
        this.isDeleted = false;
    }


    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
