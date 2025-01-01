package com.example.eventplanner.model.id;

public class ChatId {

    private Integer eventOrganizerId;
    private Integer authenticatedUserId;

    public ChatId(Integer eventOrganizerId, Integer authenticatedUserId) {
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
