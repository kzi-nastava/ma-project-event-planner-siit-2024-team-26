package com.example.eventplanner.model;

import android.os.Parcel;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;

import java.util.Calendar;
import java.util.Date;

public class Event {

    private String name;
    private String description;
    private EventType eventType;
    private Address address;
    private Calendar starts;
    private Calendar ends;
    private int guestsLimit;
    public Event(String name, String description, EventType eventType, Address address, Calendar starts, Calendar ends, int guestsLimit) {
        this.name = name;
        this.description = description;
        this.eventType = eventType;
        this.address = address;
        this.starts = starts;
        this.ends = ends;
        this.guestsLimit = guestsLimit;
    }

    public int getGuestsLimit() {
        return guestsLimit;
    }

    public void setGuestsLimit(int guestsLimit) {
        this.guestsLimit = guestsLimit;
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

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Calendar getStarts() {
        return starts;
    }

    public void setStarts(Calendar starts) {
        this.starts = starts;
    }

    public Calendar getEnds() {
        return ends;
    }

    public void setEnds(Calendar ends) {
        this.ends = ends;
    }

}
