package com.example.eventplanner.fragments.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventSearchAdapter;
import com.example.eventplanner.adapters.ImageAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.service.ServiceDetailsDTO;
import com.example.eventplanner.model.Page;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceDetailsFragment extends Fragment {

    private ServiceDetailsDTO foundService;
    private ImageAdapter imageAdapter;
    private Integer id;
    private RecyclerView imagesListView;
    private Button makeReservationButton;
    private Button addToFavouritesButton;
    private Button companyButton;
    private Button providerButton;
    private Button chatWithProviderButton;
    private Button addReviewButton;



    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ServiceDetailsFragment newInstance(Integer serviceId) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("serviceId", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        id = args.getInt("serviceId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_details, container, false);
        getServiceDetails(view);
        return view;
    }


    private void getServiceDetails(View view){
        Call<ServiceDetailsDTO> call = ClientUtils.serviceService.findService(id);
        call.enqueue(new Callback<ServiceDetailsDTO>() {

            @Override
            public void onResponse(Call<ServiceDetailsDTO> call, Response<ServiceDetailsDTO> response) {
                if (response.isSuccessful()) {
                    foundService = response.body();
                    imageAdapter = new ImageAdapter(foundService.getImages(), getContext());
                    setAttributes(view);
                }
            }

            @Override
            public void onFailure(Call<ServiceDetailsDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setAttributes(View v){
        imagesListView = v.findViewById(R.id.serviceImages);
        imagesListView.setAdapter(imageAdapter);
        makeReservationButton = v.findViewById(R.id.makeReservationButton);
        addToFavouritesButton = v.findViewById(R.id.addToFavourites);
        TextView nameTextView = v.findViewById(R.id.serviceName);
        nameTextView.setText(foundService.getName());
        TextView categoryTextView = v.findViewById(R.id.serviceCategory);
        categoryTextView.setText("Category: "+foundService.getCategoryName());
        TextView priceTextView = v.findViewById(R.id.servicePrice);
        priceTextView.setText("Price: "+String.valueOf(foundService.getPrice()) + "€");
        TextView discountTextView = v.findViewById(R.id.serviceDiscount);
        discountTextView.setText("Discount: " +String.valueOf(foundService.getDiscount())+ "%");
        TextView isAvailableTextView = v.findViewById(R.id.serviceAvailability);
        if (foundService.getAvailable()){ isAvailableTextView.setText("AVAILABLE"); }
        else { isAvailableTextView.setText("NOT AVAILABLE"); }
        TextView descriptionTextView = v.findViewById(R.id.serviceDescription);
        descriptionTextView.setText("Description: "+foundService.getDescription());
        TextView specificitiesTextView = v.findViewById(R.id.serviceSpecificities);
        specificitiesTextView.setText("Specificities: "+foundService.getSpecificity());
        companyButton = v.findViewById(R.id.serviceCompany);
        providerButton = v.findViewById(R.id.serviceProductProvideButton);
        chatWithProviderButton = v.findViewById(R.id.chatWithProviderButton);
        addReviewButton = v.findViewById(R.id.reviewButton);

    }
}