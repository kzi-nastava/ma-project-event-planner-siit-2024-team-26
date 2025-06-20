package com.example.eventplanner.dto.report;

import android.util.Log;

import com.example.eventplanner.model.Report;
import com.example.eventplanner.model.State;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class UpdateReportDTO {
    private String text;
    private State state;
    private String bannedUntil;

    public UpdateReportDTO(){
        super();
    }

    public UpdateReportDTO(String text, State state, String bannedUntil) {
        this.text = text;
        this.state = state;
        this.bannedUntil = bannedUntil;
    }

    public  UpdateReportDTO(ReportDTO report){
        this.text = report.getText();
        this.state = report.getState();
        LocalDateTime bannedUntilDateTime = LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.SECONDS);
        this.bannedUntil = bannedUntilDateTime.toString();
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

    public String getBannedUntil() {
        return bannedUntil;
    }

    public void setBannedUntil(String bannedUntil) {
        this.bannedUntil = bannedUntil;
    }
}
