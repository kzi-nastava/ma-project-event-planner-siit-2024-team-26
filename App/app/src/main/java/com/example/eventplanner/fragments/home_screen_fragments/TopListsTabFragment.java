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
import com.example.eventplanner.adapters.ProductAdapter;
import com.example.eventplanner.adapters.ServiceAdapter;
import com.example.eventplanner.adapters.ServiceProductAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.product.TopProductDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.EventType;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceProduct;
import com.example.eventplanner.service.EventService;
import com.example.eventplanner.service.ServiceService;
import com.google.gson.Gson;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopListsTabFragment extends Fragment {

    private ArrayList<TopEventDTO> topEvents;
    private ArrayList<TopServiceDTO> topServices;
    private ArrayList<TopProductDTO> topProducts;

    private EventAdapter eventAdapter;
    private ServiceAdapter serviceAdapter;
    private ProductAdapter productAdapter;


    private RecyclerView topEventsView;
    private RecyclerView topServicesView;
    private RecyclerView topProductsView;

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
        showTopServices();
        showTopProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_lists_tab, container, false);
        //Event
        topEventsView = view.findViewById(R.id.topFiveEvents);
        LinearLayoutManager layoutManagerEvents = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topEventsView.setLayoutManager(layoutManagerEvents);
        //Service
        topServicesView = view.findViewById(R.id.topFiveServices);
        LinearLayoutManager layoutManagerServices = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topServicesView.setLayoutManager(layoutManagerServices);
        //Product
        topProductsView = view.findViewById(R.id.topFiveProducts);
        LinearLayoutManager layoutManagerProducts = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        topProductsView.setLayoutManager(layoutManagerProducts);
        return view;
    }

    private void showTopEvents(){
        Call<ArrayList<TopEventDTO>> call = ClientUtils.eventService.getTopEvents();
        call.enqueue(new Callback<ArrayList<TopEventDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<TopEventDTO>> call, Response<ArrayList<TopEventDTO>> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    // Logovanje JSON odgovora
                    Log.d("JSON_RESPONSE", jsonResponse);
                    topEvents = response.body();
                    eventAdapter = new EventAdapter(topEvents, getContext());
                    topEventsView.setAdapter(eventAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopEventDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void showTopServices(){
        Call<ArrayList<TopServiceDTO>> call = ClientUtils.serviceService.getTopServices();
        call.enqueue(new Callback<ArrayList<TopServiceDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<TopServiceDTO>> call, Response<ArrayList<TopServiceDTO>> response) {
                if (response.isSuccessful()) {
                    topServices = response.body();
                    serviceAdapter = new ServiceAdapter(topServices, getContext());
                    topServicesView.setAdapter(serviceAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopServiceDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void showTopProducts(){
        Call<ArrayList<TopProductDTO>> call = ClientUtils.productService.getTopProducts();
        call.enqueue(new Callback<ArrayList<TopProductDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<TopProductDTO>> call, Response<ArrayList<TopProductDTO>> response) {
                if (response.isSuccessful()) {
                    topProducts = response.body();
                    productAdapter = new ProductAdapter(topProducts, getContext());
                    topProductsView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TopProductDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }
}
