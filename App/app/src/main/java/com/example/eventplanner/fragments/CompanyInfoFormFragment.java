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
 * Use the {@link CompanyInfoFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyInfoFormFragment extends Fragment {



    public CompanyInfoFormFragment() {
        // Required empty public constructor
    }


    public static CompanyInfoFormFragment newInstance() {
        CompanyInfoFormFragment fragment = new CompanyInfoFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_info_form, container, false);
        Button returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}