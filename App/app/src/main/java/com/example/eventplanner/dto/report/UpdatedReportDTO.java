package com.example.eventplanner.dto.report;

import com.example.eventplanner.dto.authenticatedUser.AuthenticatedUserReportDTO;
import com.example.eventplanner.model.State;

import java.time.LocalDateTime;
import java.util.Calendar;

public class UpdatedReportDTO {

    private Integer id;
    private String text;
    private State state;
    private AuthenticatedUserReportDTO reportedBy;
    private AuthenticatedUserReportDTO gotReported;
    private String bannedUntil;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public AuthenticatedUserReportDTO getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(AuthenticatedUserReportDTO reportedBy) {
        this.reportedBy = reportedBy;
    }

    public AuthenticatedUserReportDTO getGotReported() {
        return gotReported;
    }

    public void setGotReported(AuthenticatedUserReportDTO gotReported) {
        this.gotReported = gotReported;
    }

    public String getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(String bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
