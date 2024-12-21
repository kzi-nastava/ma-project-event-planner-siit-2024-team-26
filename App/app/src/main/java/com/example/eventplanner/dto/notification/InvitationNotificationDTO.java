package com.example.eventplanner.dto.notification;

public class InvitationNotificationDTO {

    private String title;
    private String description;


    public InvitationNotificationDTO(){

    }

    public InvitationNotificationDTO(String description, String title) {
        this.description = description;
        this.title = title;
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
