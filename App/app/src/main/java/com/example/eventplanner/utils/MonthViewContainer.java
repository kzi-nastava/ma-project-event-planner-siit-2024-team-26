package com.example.eventplanner.utils;

import android.view.View;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.kizitonwose.calendar.view.ViewContainer;

public class MonthViewContainer extends ViewContainer {
    public final TextView textView;

    public MonthViewContainer(View view) {
        super(view);
        textView = view.findViewById(R.id.monthHeaderText);
    }
}
