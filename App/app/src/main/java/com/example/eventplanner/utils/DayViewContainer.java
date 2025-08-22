package com.example.eventplanner.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.eventplanner.R;
import com.kizitonwose.calendar.view.ViewContainer;

public class DayViewContainer extends ViewContainer {
    public final TextView textView;

    public DayViewContainer(@NonNull View view) {
        super(view);
        textView = view.findViewById(R.id.calendarDayText);
    }
}

