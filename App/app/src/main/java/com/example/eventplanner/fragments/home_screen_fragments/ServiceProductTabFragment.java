package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceProductTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceProductTabFragment extends Fragment {

    public ServiceProductTabFragment() {
        // Required empty public constructor
    }

    public static ServiceProductTabFragment newInstance() {
        ServiceProductTabFragment fragment = new ServiceProductTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_service_product_tab, container, false);
        return view;
    }
}