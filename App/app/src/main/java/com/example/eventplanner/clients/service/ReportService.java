package com.example.eventplanner.clients.service;

import com.example.eventplanner.dto.report.ReportDTO;
import com.example.eventplanner.dto.report.UpdateReportDTO;
import com.example.eventplanner.dto.report.UpdatedReportDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReportService {

    @GET("reports/pending")
    Call<ArrayList<ReportDTO>> getPendingReports();

    @PUT("reports/{id}")
    Call<UpdatedReportDTO> updateReport(@Body UpdateReportDTO report, @Path("id") Integer id);

}
