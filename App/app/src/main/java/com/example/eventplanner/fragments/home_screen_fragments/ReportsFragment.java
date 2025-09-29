package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.NotificationAdapter;
import com.example.eventplanner.adapters.ReportAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.notification.GetNotificationDTO;
import com.example.eventplanner.dto.report.ReportDTO;
import com.example.eventplanner.model.Report;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment {


    private ArrayList<ReportDTO> allReports;
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private TextView reportsNumberTextView;


    public ReportsFragment() {
        // Required empty public constructor
    }

    public static ReportsFragment newInstance() {
        ReportsFragment fragment = new ReportsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           //
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        reportsNumberTextView = view.findViewById(R.id.reportNumber);

        setUpRecyclerView(view);
        loadReports(view);
        return view;
    }

    private void loadReports(View view){
        Call<ArrayList<ReportDTO>> call = ClientUtils.reportService.getPendingReports();
        call.enqueue(new Callback<ArrayList<ReportDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<ReportDTO>> call, Response<ArrayList<ReportDTO>> response) {
                if (response.isSuccessful()) {
                    allReports = response.body();
                    reportAdapter = new ReportAdapter(allReports, getContext(), view, reportsNumberTextView);
                    recyclerView.setAdapter(reportAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReportDTO>> call, Throwable t) {
                Log.i("Reports", t.getMessage());
            }
        });
    }

    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.reportRecyclerView);
        LinearLayoutManager layoutManagerReport= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerReport);
    }

}