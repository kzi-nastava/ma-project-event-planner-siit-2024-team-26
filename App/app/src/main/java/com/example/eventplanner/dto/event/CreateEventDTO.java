package com.example.eventplanner.dto.event;

import com.example.eventplanner.dto.address.CreateAddressDTO;
import com.example.eventplanner.dto.eventOrganizer.GetEventOrganizerDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.model.PrivacyType;

import java.time.LocalDateTime;

public class CreateEventDTO {
    private String name;
    private String description;
    private Integer guestsLimit;
    private PrivacyType privacyType;
    private String starts;
    private String ends;
    private CreateAddressDTO address;
    private GetEventTypeDTO eventType;
    private GetEventOrganizerDTO eventOrganizer;

    public CreateEventDTO(String name, String description, Integer guestsLimit, PrivacyType privacyType, String starts, String ends, CreateAddressDTO address, GetEventTypeDTO eventType, GetEventOrganizerDTO eventOrganizer) {
        this.name = name;
        this.description = description;
        this.guestsLimit = guestsLimit;
        this.privacyType = privacyType;
        this.starts = starts;
        this.ends = ends;
        this.address = address;
        this.eventType = eventType;
        this.eventOrganizer = eventOrganizer;
    }

    public CreateEventDTO() {
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

    public Integer getGuestsLimit() {
        return guestsLimit;
    }

    public void setGuestsLimit(Integer guestsLimit) {
        this.guestsLimit = guestsLimit;
    }

    public PrivacyType getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(PrivacyType privacyType) {
        this.privacyType = privacyType;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public CreateAddressDTO getAddress() {
        return address;
    }

    public void setAddress(CreateAddressDTO address) {
        this.address = address;
    }

    public GetEventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(GetEventTypeDTO eventType) {
        this.eventType = eventType;
    }

    public GetEventOrganizerDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(GetEventOrganizerDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }
}
