package com.example.eventplanner.model;

import java.util.Calendar;

public class Report {

    private Integer id;
    private String text;
    private State state;
    private AuthenticatedUser reportedBy;
    private AuthenticatedUser gotReported;
    private Calendar bannedUntil;

    public Report(String text, State state, AuthenticatedUser reportedBy, AuthenticatedUser gotReported){
        this.text = text;
        this.state = state;
        this.reportedBy = reportedBy;
        this.gotReported = gotReported;
        this.bannedUntil = null;
    }

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

    public AuthenticatedUser getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(AuthenticatedUser reportedBy) {
        this.reportedBy = reportedBy;
    }

    public AuthenticatedUser getGotReported() {
        return gotReported;
    }

    public void setGotReported(AuthenticatedUser gotReported) {
        this.gotReported = gotReported;
    }

    public Calendar getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(Calendar bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
