package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceCreationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceCreationFormFragment extends Fragment {

    CheckBox recommendNewCategoryCheckBox;
    CheckBox fixedServiceDurationCheckBox;

    EditText newCategoryEditText;
    EditText newCategoryDescriptionEditText;
    EditText minServiceEngagementEditText;
    EditText maxServiceEngagementEditText;
    EditText serviceDurationEditText;

    public ServiceCreationFormFragment() {
        // Required empty public constructor
    }

    public static ServiceCreationFormFragment newInstance() {
        ServiceCreationFormFragment fragment = new ServiceCreationFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_form, container, false);
        recommendNewCategoryCheckBox = view.findViewById(R.id.recommendNewCategoryCheckBox);
        fixedServiceDurationCheckBox = view.findViewById(R.id.fixedServiceDurationCheckBox);
        newCategoryEditText = view.findViewById(R.id.newCategory);
        newCategoryDescriptionEditText = view.findViewById(R.id.newCategoryDescription);
        minServiceEngagementEditText = view.findViewById(R.id.minEngagement);
        maxServiceEngagementEditText = view.findViewById(R.id.maxEngagement);
        serviceDurationEditText = view.findViewById(R.id.serviceDuration);

        handleButtonClicks();
        return view;
    }

    private void handleButtonClicks(){
        recommendNewCategoryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recommendNewCategoryCheckBox.isChecked()){
                    newCategoryEditText.setEnabled(true);
                    newCategoryDescriptionEditText.setEnabled(true);
                    newCategoryEditText.setBackgroundResource(R.color.white);
                    newCategoryDescriptionEditText.setBackgroundResource(R.color.white);
                }else{
                    newCategoryEditText.setEnabled(false);
                    newCategoryDescriptionEditText.setEnabled(false);
                    newCategoryEditText.setBackgroundResource(R.color.neutral);
                    newCategoryDescriptionEditText.setBackgroundResource(R.color.neutral);
                }
            }
        });
        fixedServiceDurationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fixedServiceDurationCheckBox.isChecked()){
                    minServiceEngagementEditText.setEnabled(false);
                    maxServiceEngagementEditText.setEnabled(false);
                    serviceDurationEditText.setEnabled(true);
                    minServiceEngagementEditText.setBackgroundResource(R.color.neutral);
                    maxServiceEngagementEditText.setBackgroundResource(R.color.neutral);
                    serviceDurationEditText.setBackgroundResource(R.color.white);
                }else{
                    minServiceEngagementEditText.setEnabled(true);
                    maxServiceEngagementEditText.setEnabled(true);
                    serviceDurationEditText.setEnabled(false);
                    minServiceEngagementEditText.setBackgroundResource(R.color.white);
                    maxServiceEngagementEditText.setBackgroundResource(R.color.white);
                    serviceDurationEditText.setBackgroundResource(R.color.neutral);
                }
            }
        });
    }
}