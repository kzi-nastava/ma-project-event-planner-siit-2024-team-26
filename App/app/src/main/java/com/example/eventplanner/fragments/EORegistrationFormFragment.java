package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EORegistrationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EORegistrationFormFragment extends Fragment {


    public EORegistrationFormFragment() {
        // Required empty public constructor
    }

    public static EORegistrationFormFragment newInstance() {
        EORegistrationFormFragment fragment = new EORegistrationFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eo_registration_form, container, false);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}