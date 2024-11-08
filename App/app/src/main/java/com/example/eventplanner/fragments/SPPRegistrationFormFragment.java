package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.eventplanner.R;


public class SPPRegistrationFormFragment extends Fragment {

    public SPPRegistrationFormFragment() {
        // Required empty public constructor
    }
    public static SPPRegistrationFormFragment newInstance() {
        SPPRegistrationFormFragment fragment = new SPPRegistrationFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spp_registration_form, container, false);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        Button companyInfoButton = view.findViewById(R.id.companyInfoButton);
        companyInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(CompanyInfoFormFragment.newInstance(), getActivity(), true, R.id.containerRegister);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}