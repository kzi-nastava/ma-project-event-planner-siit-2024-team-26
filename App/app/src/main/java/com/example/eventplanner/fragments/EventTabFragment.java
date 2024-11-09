package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapterSearch;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.lang.reflect.Array;
import java.util.ArrayList;
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
        events.add(new Event("Neven", "Ilincic"));
        events.add(new Event("Pera", "Peric"));
        events.add(new Event("Mika", "Mikic"));

        eventAdapterSearch = new EventAdapterSearch(getContext(), events);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_tab, container, false);

        listView = view.findViewById(R.id.foundEvents);
        listView.setAdapter(eventAdapterSearch);



        Button button = view.findViewById(R.id.eventFilterButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.FullScreenBottomSheetDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.events_search_filter, null);
                bottomSheetDialog.setContentView(dialogView);

                dialogView.getLayoutParams().height = (int) (500 * getResources().getDisplayMetrics().density);
                bottomSheetDialog.show();
            }
        });
        return view;
    }
}