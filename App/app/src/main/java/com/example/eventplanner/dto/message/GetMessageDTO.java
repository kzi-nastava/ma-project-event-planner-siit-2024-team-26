package com.example.eventplanner.dto.message;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.utils.DateStringFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GetMessageDTO {

    private Integer id;
    private ChatAuthenticatedUserDTO eventOrganizer;
    private ChatAuthenticatedUserDTO authenticatedUser;
    private String text;
    private boolean fromUser1;
    private String timeStamp;

    public GetMessageDTO(){super();}

    public GetMessageDTO(CreateMessageDTO messageDTO){
        this.id = null;
        this.eventOrganizer = messageDTO.getEventOrganizer();
        this.authenticatedUser = messageDTO.getAuthenticatedUser();
        this.fromUser1 = messageDTO.isFromUser1();
        this.text = messageDTO.getText();
        this.timeStamp = DateStringFormatter.LocalDateTimeToString(LocalDateTime.now(), "iso");
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
