package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.adapters.ServiceProductAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceProduct;
import com.example.eventplanner.service.EventService;
import com.google.gson.Gson;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopListsTabFragment extends Fragment {

    private EventService eventService;

    private ArrayList<TopEventDTO> topEvents;


    ArrayList<Event> events;
    ArrayList<ServiceProduct> services;
    ArrayList<ServiceProduct> products;
    EventAdapter eventAdapter;
    ServiceProductAdapter serviceAdapter;
    ServiceProductAdapter productAdapter;

    RecyclerView topEventsView;
    RecyclerView topServicesView;
    RecyclerView topProductsView;

    public TopListsTabFragment() {
        // Required empty public constructor
    }


    public static TopListsTabFragment newInstance() {
        TopListsTabFragment fragment = new TopListsTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showTopEvents();

        events = new ArrayList<>();
        Address myAddress = new Address("Serbia", "Novi Sad", "Partizanska", 48);
        EventType myType = new EventType("Wedding", "Wedding type!", true);
        Calendar startingDate = Calendar.getInstance();
        startingDate.set(Calendar.DAY_OF_MONTH, 5);
        startingDate.set(Calendar.MONTH, 2);
        startingDate.set(Calendar.DAY_OF_MONTH, 2);
        startingDate.set(Calendar.HOUR, 15);
        startingDate.set(Calendar.MINUTE, 30);

//        events.add(new Event("George and Sophie", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 300));
//        events.add(new Event("Luke and Nataly", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 200));
//        events.add(new Event("Jack and Lana", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 100));
//        events.add(new Event("Josh and Sophie", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 100));
//        events.add(new Event("Duke and Stephany", "Come to our wedding!", myType, myAddress, startingDate, startingDate, 100));


        products = new ArrayList<>();
        products.add(new Product("Chair", 150, 0, true, 3, "Decoration",R.drawable.download));
        products.add(new Product("Table", 300, 0, false, 4, "Decoration",R.drawable.download));
        products.add(new Product("Lamp", 150, 0, true, 3.9, "Decoration",R.drawable.download));
        products.add(new Product("Balloon", 10, 0, true, 3, "Decoration",R.drawable.download));
        products.add(new Product("Plates", 20, 0, true, 4.2, "Decoration",R.drawable.download));
        productAdapter = new ServiceProductAdapter(products);

        services = new ArrayList<>();
        services.add(new Service("Lexington Band", 200, 0, true, 4.6, "Music",R.drawable.download));
        services.add(new Service("HappyYou", 150, 0, true, 4.2, "Decoration",R.drawable.download));
        services.add(new Service("Shining", 140, 0, true, 4.2, "Lighting",R.drawable.download));
        services.add(new Service("Cleans", 140, 0, true, 4.2, "Cleaning",R.drawable.download));
        services.add(new Service("Rock&Bass Band", 140, 0, true, 4.2, "Music",R.drawable.download));
        serviceAdapter = new ServiceProductAdapter(services);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_lists_tab, container, false);
        topEventsView = view.findViewById(R.id.topFiveEvents);
        LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topEventsView.setLayoutManager(layoutManagerEvents);


        topServicesView = view.findViewById(R.id.topFiveServices);
        LinearLayoutManager layoutManagerServices = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topServicesView.setLayoutManager(layoutManagerServices);
        topServicesView.setAdapter(serviceAdapter);

        topProductsView = view.findViewById(R.id.topFiveProducts);
        LinearLayoutManager layoutManagerProducts = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topProductsView.setLayoutManager(layoutManagerProducts);
        topProductsView.setAdapter(productAdapter);
        return view;
    }

    private void showTopEvents(){
        Call<ArrayList<TopEventDTO>> call = ClientUtils.eventService.getTopEvents();
        call.enqueue(new Callback<ArrayList<TopEventDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<TopEventDTO>> call, Response<ArrayList<TopEventDTO>> response) {
                if (response.isSuccessful()) {
                    topEvents = response.body();
                    eventAdapter = new EventAdapter(topEvents);
                    topEventsView.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopEventDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }
}
