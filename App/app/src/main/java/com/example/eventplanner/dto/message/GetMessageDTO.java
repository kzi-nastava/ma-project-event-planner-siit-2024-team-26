package com.example.eventplanner.dto.message;

public class GetMessageDTO {

    private Integer id;
    private Integer eventOrganizerId;
    private Integer authenticatedUserId;
    private String text;
    private boolean fromUser1;
    private String timeStamp;

    public GetMessageDTO(){super();}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromUser1() {
        return fromUser1;
    }

    public void setFromUser1(boolean fromUser1) {
        this.fromUser1 = fromUser1;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
