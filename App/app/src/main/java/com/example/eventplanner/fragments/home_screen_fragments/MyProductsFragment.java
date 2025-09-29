package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.MyProductsAdapter;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.product.GetProductDTO;
import com.example.eventplanner.fragments.CreateProductFragment;
import com.example.eventplanner.fragments.EditProductFragment;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.ProductDetailsFragment;
import com.example.eventplanner.fragments.details.UserDetailsFragment;
import com.example.eventplanner.fragments.eventCreation.CreateEventHostFragment;
import com.example.eventplanner.viewmodel.MyProductsViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyProductsFragment extends Fragment implements MyProductsAdapter.OnProductActionClickListener {

    private MyProductsViewModel viewModel;
    private RecyclerView recyclerView;
    private MyProductsAdapter adapter;
    private ProgressBar progressBar;
    private Button btnAddProduct;
    private GetAuthenticatedUserDTO currentUser;
    private Integer loggedInUserId = 1; // Primer! Zamenite sa pravim ID-jem ulogovanog korisnika

    public static MyProductsFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        MyProductsFragment fragment = new MyProductsFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        fragment.currentUser = currentUser;
        fragment.loggedInUserId = currentUser.getId();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MyProductsViewModel.class);

        progressBar = view.findViewById(R.id.progress_bar);
        btnAddProduct = view.findViewById(R.id.btn_add_product);
        recyclerView = view.findViewById(R.id.products_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyProductsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        setupObservers();

        // Dobavi proizvode za ulogovanog korisnika
        viewModel.fetchProducts(loggedInUserId);

        btnAddProduct.setOnClickListener(v -> onCreateProductClicked());
    }

    private void setupObservers() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                adapter.updateProducts(products);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onCreateProductClicked() {
        FragmentTransition.to(
                CreateProductFragment.newInstance(currentUser),
                getActivity(),
                true,
                R.id.my_products_fragment
        );
    }

    @Override
    public void onInfoClicked(GetProductDTO product) {
        FragmentTransition.to(ProductDetailsFragment.newInstance(product.getId()), getActivity(), true, R.id.my_products_fragment);
    }

    @Override
    public void onEditClicked(GetProductDTO product) {
        FragmentTransition.to(EditProductFragment.newInstance(product.getId(), currentUser), getActivity(), true, R.id.my_products_fragment);
    }

    @Override
    public void onToggleVisibilityClicked(GetProductDTO product) {
        String action = product.isVisible() ? "invisible" : "visible";
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Action")
                .setMessage("Are you sure you want to make '" + product.getName() + "' " + action + "?")
                .setPositiveButton("Yes", (dialog, which) -> viewModel.toggleVisibility(product))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDeleteClicked(GetProductDTO product) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete '" + product.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> viewModel.deleteProduct(product))
                .setNegativeButton("Cancel", null)
                .show();
    }
}