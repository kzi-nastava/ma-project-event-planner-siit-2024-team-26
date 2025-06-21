package com.example.eventplanner.dto.report;

import com.example.eventplanner.dto.authenticatedUser.AuthenticatedUserReportDTO;
import com.example.eventplanner.model.Report;
import com.example.eventplanner.model.State;

public class ReportDTO {

    private Integer id;
    private String text;
    private AuthenticatedUserReportDTO reportedBy;
    private AuthenticatedUserReportDTO gotReported;
    private State state;

    public ReportDTO() {
        super();
    }

    public ReportDTO(Report report){
        this.id = report.getId();
        this.text = report.getText();
        this.reportedBy = new AuthenticatedUserReportDTO(report.getReportedBy());
        this.gotReported = new AuthenticatedUserReportDTO(report.getGotReported());
        this.state = report.getState();
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
