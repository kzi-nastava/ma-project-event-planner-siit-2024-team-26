package com.example.eventplanner.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.notification.UpdateNotificationDTO;
import com.example.eventplanner.dto.notification.UpdatedNotificationDTO;
import com.example.eventplanner.dto.report.ReportDTO;
import com.example.eventplanner.dto.report.UpdateReportDTO;
import com.example.eventplanner.dto.report.UpdatedReportDTO;
import com.example.eventplanner.model.Report;
import com.example.eventplanner.model.State;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder>{

    private ArrayList<ReportDTO> allReports;
    private Context context;
    private boolean[] itemExpandedState;

    private ArrayList<ReportDTO> reportsToRemove;
    private View view;

    private TextView reportsNumberTextView;

    public ReportAdapter(ArrayList<ReportDTO> allReports, Context context, View view, TextView reportsNumberTextView){
        this.allReports = allReports;
        this.context = context;
        this.itemExpandedState = new boolean[allReports.size()];
        this.reportsToRemove = new ArrayList<ReportDTO>();
        this.view = view;
        this.reportsNumberTextView = reportsNumberTextView;
        setReportsNumbertext();
    }

    @Override
    public int getItemViewType(int position){
        if (!itemExpandedState[position]){
            return 1;
        }else{
            return 2;
        }
    }

    @NonNull
    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);
                return new ReportAdapter.MyViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card_expanded, parent, false);
                return new ReportAdapter.MyViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.MyViewHolder holder, int position) {
        ReportDTO report = allReports.get(position);
        holder.reportedBy.setText(String.format("FROM: %s %s", report.getReportedBy().getFirstName(), report.getReportedBy().getLastName()));
        holder.gotReported.setText(String.format("REPORTED: %s %s", report.getGotReported().getFirstName(), report.getGotReported().getLastName()));
        holder.reportText.setText(report.getText());
        holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemExpandedState[holder.getAdapterPosition()] = !itemExpandedState[holder.getAdapterPosition()];
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ReportDTO singleReport: allReports){
                    if (report.getGotReported().getEmail().equals(singleReport.getGotReported().getEmail())){
                        reportsToRemove.add(singleReport);
                    }
                }
                for (ReportDTO report: reportsToRemove){
                    updateReport(report, State.APPROVED);
                }
                reportsToRemove.clear();
                String snackBarText = String.format("Report approved! All reports of the reported user %s %s are removed!", report.getGotReported().getFirstName(), report.getGotReported().getLastName());
                Snackbar snackbar = Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ReportDTO singleReport: allReports){
                    if (report.getGotReported().getEmail().equals(singleReport.getGotReported().getEmail())){
                        reportsToRemove.add(singleReport);
                    }
                }
                for (ReportDTO report: reportsToRemove){
                    updateReport(report, State.DISAPPROVED);
                }
                reportsToRemove.clear();
                String snackBarText = String.format("Report disapproved! All reports of the reported user %s %s are removed!", report.getGotReported().getFirstName(), report.getGotReported().getLastName());
                Snackbar snackbar = Snackbar.make(view, snackBarText, Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allReports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView reportedBy;
        TextView gotReported;
        TextView reportText;

        Button viewMoreButton;
        Button approveButton;
        Button deleteButton;


        public MyViewHolder(View itemView) {
            super(itemView);
            reportedBy = itemView.findViewById(R.id.reportReportedBy);
            gotReported = itemView.findViewById(R.id.reportGotReported);
            reportText = itemView.findViewById(R.id.reportText);
            viewMoreButton = itemView.findViewById(R.id.reportViewMoreButton);
            deleteButton = itemView.findViewById(R.id.reportDeleteButton);
            approveButton = itemView.findViewById(R.id.reportApproveButton);
        }
    }

    private void updateReport(ReportDTO report, State newState){
        UpdateReportDTO updateReport = new UpdateReportDTO(report, newState);
        updateReport.setState(newState);
        Call<UpdatedReportDTO> call = ClientUtils.reportService.updateReport(updateReport, report.getId());
        call.enqueue(new Callback<UpdatedReportDTO>() {

            @Override
            public void onResponse(Call<UpdatedReportDTO> call, Response<UpdatedReportDTO> response) {
                if (response.isSuccessful()) {
                    int i = allReports.indexOf(report);
                    allReports.remove(i);
                    notifyItemRemoved(i);
                    setReportsNumbertext();
                }
            }

            @Override
            public void onFailure(Call<UpdatedReportDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setReportsNumbertext(){
        if (allReports.isEmpty()){
            reportsNumberTextView.setText("There's no reports!");
        }else if (allReports.size() == 1){
            reportsNumberTextView.setText("There's 1 report!");
        }else{
            reportsNumberTextView.setText(String.format("There's %d reports!", allReports.size()));
        }
    }
}
