package com.example.eventplanner.dto.event;

import com.example.eventplanner.dto.address.CreatedAddressDTO;
import com.example.eventplanner.dto.eventOrganizer.GetEventOrganizerDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.model.PrivacyType;

public class CreatedEventDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer guestsLimit;
    private PrivacyType privacyType;
    private String starts;
    private String ends;
    private CreatedAddressDTO address;

    public CreatedEventDTO() {
    }

    private GetEventTypeDTO eventType;
    private GetEventOrganizerDTO eventOrganizer;

    public CreatedEventDTO(Integer id, String name, String description, Integer guestsLimit, PrivacyType privacyType, String starts, String ends, CreatedAddressDTO address, GetEventTypeDTO eventType, GetEventOrganizerDTO eventOrganizer) {
        this.id = id;
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

    public CreatedAddressDTO getAddress() {
        return address;
    }

    public void setAddress(CreatedAddressDTO address) {
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
