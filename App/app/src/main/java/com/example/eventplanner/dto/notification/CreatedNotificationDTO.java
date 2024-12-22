package com.example.eventplanner.dto.notification;

import java.time.LocalDateTime;

public class CreatedNotificationDTO {
    private Integer id;
    private Integer receiverId;
    private String title;
    private String text;
    private LocalDateTime timeStamp;
    private boolean isRead;
    private boolean isDeleted;

    public CreatedNotificationDTO(){super();}

    public CreatedNotificationDTO(Integer id, Integer receiverId, String title, String text, LocalDateTime timeStamp, boolean isRead, boolean isDeleted) {
        this.id = id;
        this.receiverId = receiverId;
        this.title = title;
        this.text = text;
        this.timeStamp = timeStamp;
        this.isRead = isRead;
        this.isDeleted = isDeleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
