package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.HomeActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.fragments.FragmentTransition;


public class HomeScreenFragment extends Fragment {

    public HomeScreenFragment() {
        // Required empty public constructor
    }


    public static HomeScreenFragment newInstance() {
        HomeScreenFragment fragment = new HomeScreenFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        //Function that handles tabs selection
        setUpTabs(view);
        return view;
    }
    private void setUpTabs(View v){
        TextView trendingText = v.findViewById(R.id.trendingText);
        TextView eventsText = v.findViewById(R.id.eventsText);
        TextView servicesProductText = v.findViewById(R.id.servicesAndProductsText);

        //Put fragment on opening this activity
        trendingText.setTextColor(getResources().getColor(R.color.black));
        FragmentTransition.to(TopListsTabFragment.newInstance(), getActivity(), false, R.id.homeScreenFragment);

        eventsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetColors(trendingText, eventsText, servicesProductText);
                eventsText.setTextColor(getResources().getColor(R.color.black));
                FragmentTransition.to(EventTabFragment.newInstance(), getActivity(), false, R.id.homeScreenFragment);
            }
        });

        trendingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetColors(trendingText, eventsText, servicesProductText);
                trendingText.setTextColor(getResources().getColor(R.color.black));
                FragmentTransition.to(TopListsTabFragment.newInstance(), getActivity(), false, R.id.homeScreenFragment);
            }
        });

        servicesProductText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetColors(trendingText, eventsText, servicesProductText);
                servicesProductText.setTextColor(getResources().getColor(R.color.black));
                FragmentTransition.to(ServiceProductTabFragment.newInstance(), getActivity(), false, R.id.homeScreenFragment);
            }
        });
    }

    private void resetColors(TextView trendingText, TextView eventsText, TextView servicesProductsText){
        trendingText.setTextColor(getResources().getColor(R.color.white));
        eventsText.setTextColor(getResources().getColor(R.color.white));
        servicesProductsText.setTextColor(getResources().getColor(R.color.white));
    }
}