package com.example.eventplanner.dto.event;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.model.EventOrganizer;

import java.time.LocalDateTime;
import java.util.List;


public class TopEventDTO {
    private Integer id;
    private String name;
    private String description;
    private String starts;
    private List<String> images;
    private ChatAuthenticatedUserDTO eventOrganizer;

    public TopEventDTO(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public ChatAuthenticatedUserDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(ChatAuthenticatedUserDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
}
