package com.example.eventplanner.dto.notification;

import com.example.eventplanner.utils.DateStringFormatter;

import java.time.LocalDateTime;

public class UpdateNotificationDTO {
    private String title;
    private String text;
    private String timeStamp;
    private boolean read;
    private boolean deleted;

    public UpdateNotificationDTO(){super();}

    public UpdateNotificationDTO(GetNotificationDTO notification){
        this.title = notification.getTitle();
        this.text = notification.getText();
        this.timeStamp = notification.getTimeStamp();
        this.read = notification.isRead();
        this.deleted = notification.isDeleted();
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        read = read;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        deleted = deleted;
    }
}
