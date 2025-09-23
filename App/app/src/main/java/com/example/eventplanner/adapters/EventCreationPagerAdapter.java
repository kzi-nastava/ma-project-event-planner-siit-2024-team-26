package com.example.eventplanner.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.eventplanner.fragments.eventCreation.*; // Import all step fragments

public class EventCreationPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 5;

    public EventCreationPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Step1EventInfoFragment();
            case 1:
                return new Step2LocationTimeFragment();
            case 2:
                // This is a placeholder as the original component was not provided
                return new Step3InvitationsFragment();
            case 3:
                // This is a placeholder as the original component was not provided
                return new Step4ActivitiesFragment();
            case 4:
                return new Step5BudgetFragment();
            default:
                throw new IllegalStateException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}