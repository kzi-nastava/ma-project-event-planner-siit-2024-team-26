package com.example.eventplanner.fragments.home_screen_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ServiceProductAdapter;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceProduct;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceProductTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceProductTabFragment extends Fragment {

    ArrayList<ServiceProduct> serviceProducts;
    ServiceProductAdapter serviceProductAdapter;
    RecyclerView recyclerView;

    public ServiceProductTabFragment() {
        // Required empty public constructor
    }

    public static ServiceProductTabFragment newInstance() {
        ServiceProductTabFragment fragment = new ServiceProductTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        serviceProducts = new ArrayList<>();
        serviceProducts.add(new Product("Chair", 150, 0, true, 3, "Decoration",R.drawable.download));
        serviceProducts.add(new Product("Table", 300, 0, false, 4, "Decoration",R.drawable.download));
        serviceProducts.add(new Product("Lamp", 150, 0, true, 3.9, "Decoration",R.drawable.download));

        serviceProducts.add(new Service("Lexington Band", 200, 0, true, 4.6, "Music",R.drawable.download));
        serviceProducts.add(new Service("HappyYou", 150, 0, true, 4.2, "Decoration",R.drawable.download));
        serviceProducts.add(new Service("Shining", 140, 0, true, 4.2, "Lighting",R.drawable.download));

        serviceProductAdapter = new ServiceProductAdapter(serviceProducts);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_product_tab, container, false);

        recyclerView = view.findViewById(R.id.foundServiceProducts);
        LinearLayoutManager layoutManagerServiceProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerServiceProduct);
        recyclerView.setAdapter(serviceProductAdapter);

        Button filterButton = view.findViewById(R.id.serviceProductFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.FullScreenBottomSheetDialog);
                View dialogView = getLayoutInflater().inflate(R.layout.services_products_search_filter, null);
                bottomSheetDialog.setContentView(dialogView);
                dialogView.getLayoutParams().height = (int) (800 * getResources().getDisplayMetrics().density);
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setDraggable(false); // Onemogući prevlačenje

                }

                String[] optionsArray = {"Stuff", "Decoration", "Music"};
                boolean[] checkedItems = {false, false, false};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Category")
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

                TextView textView = dialogView.findViewById(R.id.serviceProductCategory);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.create().show();
                    }
                });

                bottomSheetDialog.show();
            }
        });

        return view;
    }
}