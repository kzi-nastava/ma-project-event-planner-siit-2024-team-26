package com.example.eventplanner.dto.event;

public class EventReservationDTO {
    private Integer id;
    private String name;
    private String description;
    private String starts;

    public EventReservationDTO(){super();}

    public EventReservationDTO(Integer id, String name, String description, String starts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.starts = starts;
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
}
