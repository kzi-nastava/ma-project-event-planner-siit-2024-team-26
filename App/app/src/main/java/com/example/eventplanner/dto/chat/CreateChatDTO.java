package com.example.eventplanner.dto.chat;

public class CreateChatDTO {

    private Integer eventOrganizerId;
    private Integer authenticatedUserId;

    public CreateChatDTO(Integer eventOrganizerId, Integer authenticatedUserId) {
        this.eventOrganizerId = eventOrganizerId;
        this.authenticatedUserId = authenticatedUserId;
    }

    public Integer getEventOrganizerId() {
        return eventOrganizerId;
    }

    public void setEventOrganizerId(Integer eventOrganizerId) {
        this.eventOrganizerId = eventOrganizerId;
    }

    public Integer getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(Integer authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }
}
