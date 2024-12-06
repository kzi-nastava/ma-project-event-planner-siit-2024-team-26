package com.example.eventplanner.fragments.home_screen_fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.EventType;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.example.eventplanner.model.Event;

public class EventTabFragment extends Fragment {


    private EventAdapter eventAdapter;
    private ArrayList<Event> events;
    private RecyclerView recyclerView;

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
        EventType myType = new EventType("Wedding", "Wedding type!", true);
        Calendar startingDate = Calendar.getInstance();
        startingDate.set(Calendar.DAY_OF_MONTH, 5);
        startingDate.set(Calendar.MONTH, 2);
        startingDate.set(Calendar.DAY_OF_MONTH, 2);
        startingDate.set(Calendar.HOUR, 15);
        startingDate.set(Calendar.MINUTE, 30);


        events.add(new Event("George and Sophie", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 300));
        events.add(new Event("Luke and Nataly", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 200));
        events.add(new Event("Jack and Lana", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 100));

//        eventAdapter = new EventAdapter(events);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_tab, container, false);

        recyclerView = view.findViewById(R.id.foundEvents);
        LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerEvents);
        recyclerView.setAdapter(eventAdapter);

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

                String[] optionsArray = {"Wedding", "Conference", "Birthday"};
                boolean[] checkedItems = {false, false, false};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Event types")
                        .setMultiChoiceItems(optionsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // Ovdje možete pratiti promene stanja odabira (ako je stavka selektovana ili ne)
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Na klik na "OK", uradite nešto sa selektovanim opcijama
                            }
                        })
                        .setNegativeButton("Cancel", null);

                TextView textView = dialogView.findViewById(R.id.eventTypeSearch);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.create().show();
                    }
                });

                Button date1Button = dialogView.findViewById(R.id.date1button);
                date1Button.setOnClickListener(view -> showDatePickerDialog());
                Button date2Button = dialogView.findViewById(R.id.date2button);
                date2Button.setOnClickListener(view -> showDatePickerDialog());

                Spinner spinnerCriteria = dialogView.findViewById(R.id.sortBy);
                String[] sortCriteria = {"Name", "Type", "Starting date", "Ending date"};
                ArrayAdapter<String> criteriaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_selected_item, sortCriteria);
                criteriaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerCriteria.setAdapter(criteriaAdapter);

                Spinner spinnerOrder = dialogView.findViewById(R.id.sortOrder);
                String[] sortOrder = {"ASC", "DESC"};
                ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_selected_item, sortOrder);
                orderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerOrder.setAdapter(orderAdapter);


                bottomSheetDialog.show();
            }
        });
        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(), // Use requireActivity() for the context
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    Toast.makeText(requireContext(), "Selected Date: " + date, Toast.LENGTH_SHORT).show();
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}