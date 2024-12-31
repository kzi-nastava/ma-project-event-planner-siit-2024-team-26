package com.example.eventplanner.dto.event;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.model.PrivacyType;

import java.time.LocalDateTime;

public class GetEventDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer guestsLimit;
    private PrivacyType privacyType;
    private String starts;
    private String ends;
    private Integer gradeSum;
    private Integer gradeCount;
    private Integer numVisitors;
    private GetAddressDTO address;
    private GetEventTypeDTO eventType;

    public GetEventDTO(){super();}

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

    public Integer getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(Integer gradeSum) {
        this.gradeSum = gradeSum;
    }

    public Integer getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(Integer gradeCount) {
        this.gradeCount = gradeCount;
    }

    public Integer getNumVisitors() {
        return numVisitors;
    }

    public void setNumVisitors(Integer numVisitors) {
        this.numVisitors = numVisitors;
    }

    public GetAddressDTO getAddress() {
        return address;
    }

    public void setAddress(GetAddressDTO address) {
        this.address = address;
    }

    public GetEventTypeDTO getEventType() {
        return eventType;
    }

    public void setEventType(GetEventTypeDTO eventType) {
        this.eventType = eventType;
    }
}
