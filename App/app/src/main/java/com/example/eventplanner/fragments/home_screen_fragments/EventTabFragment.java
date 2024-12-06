package com.example.eventplanner.fragments.home_screen_fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.adapters.EventSearchAdapter;
import com.example.eventplanner.adapters.ServiceAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Page;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.example.eventplanner.model.Event;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTabFragment extends Fragment {


    private EventSearchAdapter eventAdapter;
    private List<EventCardDTO> foundEvents;
    private RecyclerView recyclerView;

    String name;
    List<String> selectedCities;
    List<String> selectedEventTypes;
    String selectedDateNotBefore;
    String selectedDateNotAfter;
    String sortDirection;

    Integer totalPages;
    Integer currentPage;

    View mainView;
    Button nextButton;
    Button previousButton;

    Boolean canNext;
    Boolean canPrevious;

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
        this.totalPages = 0;
        this.currentPage = 0;

        this.name = "";
        this.selectedDateNotAfter =  "01.01.1000.";
        selectedEventTypes = new ArrayList<>();
        selectedCities = new ArrayList<>();
        this.selectedDateNotAfter = "30.12.3000.";


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_tab, container, false);

        mainView = view;
        previousButton = mainView.findViewById(R.id.previousEventsButton);
        nextButton = mainView.findViewById(R.id.nextEventsButton);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canPrevious){
                    currentPage -= 1;
                    makeSearch();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canNext){
                    currentPage += 1;
                    makeSearch();
                }
            }
        });

        recyclerView = view.findViewById(R.id.foundEvents);
        LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerEvents);

        makeSearch();

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

                selectedEventTypes = new ArrayList<>();
                selectedCities = new ArrayList<>();
                selectedDateNotBefore = "01.01.1000.";
                selectedDateNotAfter = "30.12.3000.";
                //EVENT TYPES
                String[] optionsArray = {"Workshop", "Conference", "Birthday"};
                ArrayList<String> options = new ArrayList<>();
                for (String s : optionsArray){ // THIS SHOULD BE BASED ON EXISTING EVENT TYPES
                    options.add(s);
                }

                boolean[] checkedItems = {false, false, false};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Event types")
                        .setMultiChoiceItems(optionsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked){
                                    selectedEventTypes.add(options.get(which));
                                }
                                else{
                                    selectedEventTypes.remove(options.get(which));
                                }
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

                //CITIES
                String[] optionsCitiesList = {"Novi Sad", "Belgrade", "London"};
                ArrayList<String> optionsCities = new ArrayList<>();
                for (String c : optionsCitiesList){
                    optionsCities.add(c);
                }

                boolean[] checkedItemsCities = {false, false, false};
                AlertDialog.Builder builderCities = new AlertDialog.Builder(getContext());
                builderCities.setTitle("Cities")
                        .setMultiChoiceItems(optionsCitiesList, checkedItemsCities, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // Ovdje možete pratiti promene stanja odabira (ako je stavka selektovana ili ne)
                                if (isChecked){
                                    selectedCities.add(optionsCities.get(which));
                                }
                                else{
                                    selectedCities.remove(optionsCities.get(which));
                                    Log.i("TESTA", "SS");
                                }
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Na klik na "OK", uradite nešto sa selektovanim opcijama
                            }
                        })
                        .setNegativeButton("Cancel", null);

                TextView citiesTextView = dialogView.findViewById(R.id.citiesSearch);
                citiesTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builderCities.create().show();
                    }
                });


                Button date1Button = dialogView.findViewById(R.id.notBeforeButton);
                date1Button.setOnClickListener(view -> showDatePickerDialog("notBefore"));
                Button date2Button = dialogView.findViewById(R.id.notAfterButton);
                date2Button.setOnClickListener(view -> showDatePickerDialog("notAfter"));

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

                Button searchButton = dialogView.findViewById(R.id.eventsFilterSearchButton);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchEvents(dialogView, spinnerOrder);
                    }
                });


                bottomSheetDialog.show();
            }
        });
        return view;
    }

    private void showDatePickerDialog(String which) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(), // Use requireActivity() for the context
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    if(which.equals("notBefore")){
                    selectedDateNotBefore = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear + ".";
                    Toast.makeText(requireContext(), "Not before: " + selectedDateNotBefore, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        selectedDateNotAfter = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear + ".";
                        Toast.makeText(requireContext(), "Not after: " + selectedDateNotAfter, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void searchEvents(View v, Spinner spinnerOrder){
        sortDirection = spinnerOrder.getSelectedItem().toString();
        EditText nameSearch = v.findViewById(R.id.nameSearchEvents);

        name = nameSearch.getText().toString();

        makeSearch();
    }

    private void makeSearch(){
        Call<Page<EventCardDTO>> call = ClientUtils.eventService.searchEvents(name, selectedDateNotBefore, selectedDateNotAfter, selectedEventTypes, selectedCities, sortDirection, 1, currentPage);
        call.enqueue(new Callback<Page<EventCardDTO>>() {

            @Override
            public void onResponse(Call<Page<EventCardDTO>> call, Response<Page<EventCardDTO>> response) {
                if (response.isSuccessful()) {
                    foundEvents = response.body().getContent();
                    totalPages = response.body().getTotalPages();
                    ArrayList<EventCardDTO> foundEventsArrayList = new ArrayList<>(foundEvents);
                    eventAdapter = new EventSearchAdapter(foundEventsArrayList, getContext());
                    recyclerView.setAdapter(eventAdapter);
                    setUpPageButtonsAvailability();
                }
            }

            @Override
            public void onFailure(Call<Page<EventCardDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setUpPageButtonsAvailability(){
        if (currentPage == 0){ previousButton.setAlpha(0.5f); canPrevious = false; }
        else{ previousButton.setAlpha(1); canPrevious = true; }
        if (currentPage == totalPages - 1){ nextButton.setAlpha(0.5f); canNext = false;}
        else{ nextButton.setAlpha(1); canNext = true;}
    }

}