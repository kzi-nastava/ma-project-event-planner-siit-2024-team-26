package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceCreationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceCreationFormFragment extends Fragment {


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
        return view;
    }
}