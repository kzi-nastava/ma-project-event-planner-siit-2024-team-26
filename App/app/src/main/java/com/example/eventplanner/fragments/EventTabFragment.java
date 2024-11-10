package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapterSearch;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.EventType;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.eventplanner.model.Event;

public class EventTabFragment extends Fragment {


    private EventAdapterSearch eventAdapterSearch;
    private ArrayList<Event> events;

    private ListView listView;

    public EventTabFragment() {
        // Required empty public constructor
    }


    public static EventTabFragment newInstance() {
        EventTabFragment fragment = new EventTabFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events = new ArrayList<>();
        Address myAddress = new Address("Serbia", "Novi Sad", "Partizanska", 48);
        EventType myType = new EventType("Svadba", "Dodjite!", true);
        Calendar startingDate = Calendar.getInstance();
        startingDate.set(Calendar.DAY_OF_MONTH, 5);
        startingDate.set(Calendar.MONTH, 2);
        startingDate.set(Calendar.DAY_OF_MONTH, 2);
        startingDate.set(Calendar.HOUR, 15);
        startingDate.set(Calendar.MINUTE, 30);


        events.add(new Event("Neven", "Ilincic", myType, myAddress, startingDate, startingDate, 300));
        events.add(new Event("Pera", "Peric", myType, myAddress, startingDate, startingDate, 200));
        events.add(new Event("Mika", "Mikic", myType, myAddress, startingDate, startingDate, 100));

        eventAdapterSearch = new EventAdapterSearch(getContext(), events);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_tab, container, false);

        listView = view.findViewById(R.id.foundEvents);
        listView.setAdapter(eventAdapterSearch);


        Locale locale = new Locale("en", "US");
        Locale.setDefault(locale);

        // Setovanje nove lokalizacije za aplikaciju
        android.content.res.Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        Button button = view.findViewById(R.id.eventFilterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.FullScreenBottomSheetDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.events_search_filter, null);
                bottomSheetDialog.setContentView(dialogView);
                dialogView.getLayoutParams().height = (int) (800 * getResources().getDisplayMetrics().density);
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setDraggable(false); // Onemogući prevlačenje

                }
                bottomSheetDialog.show();
            }
        });
        return view;
    }
}