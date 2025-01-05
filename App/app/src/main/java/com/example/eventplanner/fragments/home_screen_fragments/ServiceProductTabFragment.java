package com.example.eventplanner.fragments.home_screen_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ProductSearchAdapter;
import com.example.eventplanner.adapters.ServiceProductSearchAdapter;
import com.example.eventplanner.adapters.ServiceSearchAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.product.ProductCardDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.model.Page;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceProductTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceProductTabFragment extends Fragment {

    private GetAuthenticatedUserDTO currentUser;

    List<ServiceProductCardDTO> foundServicesProducts;
    List<ServiceCardDTO> foundServices;
    List<ProductCardDTO> foundProducts;

    ServiceProductSearchAdapter serviceProductSearchAdapter;
    RecyclerView recyclerView;

    ServiceProductSearchAdapter serviceProductAdapter;
    ServiceSearchAdapter serviceAdapter;
    ProductSearchAdapter productAdapter;


    String name;
    ArrayList<String> selectedCategories;
    Integer minPrice;
    Integer maxPrice;
    String sortDirection;
    Integer minDurationService;
    Integer maxDurationService;
    RadioGroup onlyRadioGroup;

    Integer totalPages;
    Integer currentPage;
    Button previousButton;
    Button nextButton;
    Boolean canPrevious;
    Boolean canNext;
    public ServiceProductTabFragment() {
        // Required empty public constructor
    }

    public static ServiceProductTabFragment newInstance(GetAuthenticatedUserDTO user) {
        ServiceProductTabFragment fragment = new ServiceProductTabFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            currentUser = getArguments().getParcelable("currentUser");
        }

        this.totalPages = 0;
        this.currentPage = 0;

        this.name = "";
        this.minPrice = 0;
        this.maxPrice = 999999;
        selectedCategories = new ArrayList<>();
        this.minDurationService = 0;
        this.maxDurationService = 99999;
        this.sortDirection = "ASC";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_product_tab, container, false);

        recyclerView = view.findViewById(R.id.foundServiceProducts);
        LinearLayoutManager layoutManagerServiceProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerServiceProduct);
        recyclerView.setAdapter(serviceProductSearchAdapter);

        previousButton = view.findViewById(R.id.previousServiceProductButton);
        nextButton = view.findViewById(R.id.nextServiceProductsButton);


        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canPrevious){
                    currentPage -= 1;
                    callRightSearch();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canNext){
                    currentPage += 1;
                    callRightSearch();
                }
            }
        });


        callRightSearch();

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

                selectedCategories = new ArrayList<>();
                onlyRadioGroup = dialogView.findViewById(R.id.onlyRadioGroup);

                //CATEGORIES
                String[] optionsArray = {"Wedding", "Birthday", "Decoration"};
                ArrayList<String> options = new ArrayList<>();
                for (String s : optionsArray){ // THIS SHOULD BE BASED ON EXISTING CATEGORIES
                    options.add(s);
                }
                boolean[] checkedItems = {false, false, false};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Categories")
                        .setMultiChoiceItems(optionsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked){
                                    selectedCategories.add(options.get(which));
                                }
                                else{
                                    selectedCategories.remove(options.get(which));
                                }
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

                Spinner spinnerCriteria = dialogView.findViewById(R.id.sortBy);
                String[] sortCriteria = {"Name", "Category", "Min price", "Max price", "Discount", "Min duration", "Max duration"};
                ArrayAdapter<String> criteriaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_selected_item, sortCriteria);
                criteriaAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerCriteria.setAdapter(criteriaAdapter);

                Spinner spinnerOrder = dialogView.findViewById(R.id.sortOrder);
                String[] sortOrder = {"ASC", "DESC"};
                ArrayAdapter<String> orderAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_selected_item, sortOrder);
                orderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                spinnerOrder.setAdapter(orderAdapter);


                Button searchButton = dialogView.findViewById(R.id.serviceProductsFilterSearchButton);
                searchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentPage = 0;
                        makeSearch(dialogView, spinnerOrder);
                        bottomSheetDialog.cancel();
                    }
                });

                bottomSheetDialog.show();
            }
        });

        return view;
    }

    private void makeSearch(View v, Spinner sortOrder){
        EditText nameSearch = v.findViewById(R.id.nameSearch);
        EditText minPriceSearch = v.findViewById(R.id.serviceProductMinPrice);
        EditText maxPriceSearch = v.findViewById(R.id.serviceProductMaxPrice);
        EditText minDurationSearch = v.findViewById(R.id.serviceMinDuration);
        EditText maxDurationSearch = v.findViewById(R.id.serviceMaxDuration);

        name = nameSearch.getText().toString();
        if (minPriceSearch.getText().toString().equals("")){ minPrice = 0; }
        else{minPrice = Integer.parseInt(minPriceSearch.getText().toString()); }
        if (maxPriceSearch.getText().toString().equals("")) { maxPrice = 99999; }
        else{ maxPrice = Integer.parseInt(maxPriceSearch.getText().toString()); }
        if (minDurationSearch.getText().toString().equals("")) { minDurationService = 0; }
        else{ minDurationService = Integer.parseInt(minDurationSearch.getText().toString()); }
        if (maxDurationSearch.getText().toString().equals("")) { maxDurationService = 99999; }
        else { maxDurationService = Integer.parseInt(maxDurationSearch.getText().toString()); }
        sortDirection = sortOrder.getSelectedItem().toString();

        callRightSearch();
    }

    private void callRightSearch(){
        int selectedRadioButton = R.id.showBothRadioButton;
        if (onlyRadioGroup != null){
            selectedRadioButton = onlyRadioGroup.getCheckedRadioButtonId();
        }
        if (selectedRadioButton == R.id.showBothRadioButton){
            searchServicesAndProducts();
        }
        else if (selectedRadioButton == R.id.onlyServicesRadioButton){
            searchServices();
        } else{
            searchProducts();
        }

    }
    private void setUpPageButtonsAvailability(){
        if (currentPage == 0 || totalPages == 0){ previousButton.setAlpha(0.5f); canPrevious = false; }
        else{ previousButton.setAlpha(1); canPrevious = true; }
        if (currentPage == totalPages - 1 || totalPages == 0){ nextButton.setAlpha(0.5f); canNext = false;}
        else{ nextButton.setAlpha(1); canNext = true;}
    }


    private void searchServicesAndProducts(){
        Call<Page<ServiceProductCardDTO>> call = ClientUtils.serviceProductService.searchServicesAndProducts(name, minPrice, maxPrice, selectedCategories, sortDirection, 5, currentPage);
        call.enqueue(new Callback<Page<ServiceProductCardDTO>>() {

            @Override
            public void onResponse(Call<Page<ServiceProductCardDTO>> call, Response<Page<ServiceProductCardDTO>> response) {
                if (response.isSuccessful()) {

                    foundServicesProducts = response.body().getContent();

                    totalPages = response.body().getTotalPages();
                    ArrayList<ServiceProductCardDTO> foundServicesProductsArrayList = new ArrayList<>(foundServicesProducts);
                    serviceProductAdapter = new ServiceProductSearchAdapter(foundServicesProductsArrayList, getContext(), getActivity(), currentUser);
                    recyclerView.setAdapter(serviceProductAdapter);
                    setUpPageButtonsAvailability();
                }
            }

            @Override
            public void onFailure(Call<Page<ServiceProductCardDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void searchServices(){
        Call<Page<ServiceCardDTO>> call = ClientUtils.serviceService.searchServices(name, minPrice, maxPrice, minDurationService, maxDurationService, selectedCategories, sortDirection, 5, currentPage);
        call.enqueue(new Callback<Page<ServiceCardDTO>>() {

            @Override
            public void onResponse(Call<Page<ServiceCardDTO>> call, Response<Page<ServiceCardDTO>> response) {
                if (response.isSuccessful()) {

                    foundServices = response.body().getContent();

                    totalPages = response.body().getTotalPages();
                    ArrayList<ServiceCardDTO> foundServicesArrayList = new ArrayList<>(foundServices);
                    serviceAdapter = new ServiceSearchAdapter(foundServicesArrayList, getContext(), getActivity(), currentUser);
                    recyclerView.setAdapter(serviceAdapter);
                    setUpPageButtonsAvailability();
                }
            }

            @Override
            public void onFailure(Call<Page<ServiceCardDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void searchProducts(){
        Call<Page<ProductCardDTO>> call = ClientUtils.productService.searchProducts(name, minPrice, maxPrice, selectedCategories, sortDirection, 5, currentPage);
        call.enqueue(new Callback<Page<ProductCardDTO>>() {

            @Override
            public void onResponse(Call<Page<ProductCardDTO>> call, Response<Page<ProductCardDTO>> response) {
                if (response.isSuccessful()) {
                    foundProducts = response.body().getContent();
                    totalPages = response.body().getTotalPages();
                    ArrayList<ProductCardDTO> foundProductsArrayList = new ArrayList<>(foundProducts);
                    productAdapter = new ProductSearchAdapter(foundProductsArrayList, getContext());
                    recyclerView.setAdapter(productAdapter);
                    setUpPageButtonsAvailability();
                }
            }

            @Override
            public void onFailure(Call<Page<ProductCardDTO>> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }
}