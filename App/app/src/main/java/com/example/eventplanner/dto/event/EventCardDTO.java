package com.example.eventplanner.dto.event;

import java.time.LocalDateTime;

public class EventCardDTO {

    private Integer id;
    private String name;
    private String image;
    private String starts;

    public EventCardDTO(){}

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }
}
