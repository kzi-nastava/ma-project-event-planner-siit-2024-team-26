package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentEditProductBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.viewmodel.EditProductViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.stream.Collectors;

public class EditProductFragment extends Fragment {

    private EditProductViewModel viewModel;
    private FragmentEditProductBinding binding;
    private Integer productId;
    private GetAuthenticatedUserDTO user;

    public static EditProductFragment newInstance(int productId, GetAuthenticatedUserDTO user) {
        EditProductFragment fragment = new EditProductFragment();
        Bundle args = new Bundle();
        args.putInt("PRODUCT_ID", productId);
        fragment.setArguments(args);
        fragment.user = user;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            productId = getArguments().getInt("PRODUCT_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditProductViewModel.class);
        viewModel.user = user;

        setupListeners();
        setupObservers();

        if (productId != null) {
            viewModel.loadProductAndCategories(productId);
        } else {
            Toast.makeText(getContext(), "Error: Product ID is missing.", Toast.LENGTH_LONG).show();
            getParentFragmentManager().popBackStack();
        }
    }

    private void setupListeners() {
        // Povezivanje unosa sa ViewModel-om
        binding.etDescription.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.description.setValue(text)));
        binding.etPrice.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.price.setValue(text)));
        binding.etDiscount.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.discount.setValue(text)));
        binding.etProposedCategory.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.proposedCategory.setValue(text)));

        binding.rgAvailable.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.isAvailable.setValue(checkedId == R.id.rb_available_yes);
        });
        binding.rgVisible.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.isVisible.setValue(checkedId == R.id.rb_visible_yes);
        });

        binding.actvCategory.setOnItemClickListener((parent, view, position, id) -> {
            boolean isProposing = position == 0;
            viewModel.isProposingCategory.setValue(isProposing);
            if (!isProposing) {
                viewModel.selectedCategoryId.setValue(viewModel.getCategories().getValue().get(position - 1).getId());
            } else {
                viewModel.selectedCategoryId.setValue(null);
            }
        });

        // Listener-i za dugmad
        binding.btnUpdate.setOnClickListener(v -> viewModel.updateProduct());
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        });

        viewModel.getUpdateSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Vrati se na prethodni ekran nakon uspešnog ažuriranja
                getParentFragmentManager().popBackStack();
            }
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                updateCategoryDropdown(categories);
            }
        });

        viewModel.isProposingCategory.observe(getViewLifecycleOwner(), isProposing -> {
            binding.layoutProposedCategory.setVisibility(isProposing ? View.VISIBLE : View.GONE);
        });

        // Glavni observer koji popunjava formu kada stignu podaci o proizvodu
        viewModel.getProduct().observe(getViewLifecycleOwner(), product -> {
            if (product != null) {
                binding.toolbar.setTitle("Edit: " + product.getName());
                binding.etDescription.setText(product.getDescription());
                binding.etPrice.setText(String.valueOf(product.getPrice()));
                binding.etDiscount.setText(String.valueOf(product.getDiscount()));

                binding.rgAvailable.check(product.isAvailable() ? R.id.rb_available_yes : R.id.rb_available_no);
                binding.rgVisible.check(product.isVisible() ? R.id.rb_visible_yes : R.id.rb_visible_no);

                // Postavi selektovanu kategoriju u dropdown-u
                updateCategorySelection();
            }
        });
    }

    private void updateCategoryDropdown(List<GetCategoryDTO> categories) {
        List<String> categoryNames = categories.stream().map(GetCategoryDTO::getName).collect(Collectors.toList());
        categoryNames.add(0, "-- Propose new category --");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        binding.actvCategory.setAdapter(adapter);

        // Ažuriraj selekciju ako su podaci o proizvodu već stigli
        updateCategorySelection();
    }

    private void updateCategorySelection() {
        Integer categoryId = viewModel.selectedCategoryId.getValue();
        List<GetCategoryDTO> categories = viewModel.getCategories().getValue();

        if (categoryId != null && categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId().equals(categoryId)) {
                    String categoryName = categories.get(i).getName();
                    binding.actvCategory.setText(categoryName, false);
                    return;
                }
            }
        }
    }

    // Helper TextWatcher klasa
    private static class SimpleTextWatcher implements TextWatcher {
        private final OnTextChanged listener;
        public SimpleTextWatcher(OnTextChanged listener) { this.listener = listener; }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { listener.onTextChanged(s.toString()); }
        interface OnTextChanged { void onTextChanged(String text); }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Sprečava curenje memorije
    }
}