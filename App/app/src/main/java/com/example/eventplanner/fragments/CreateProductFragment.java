package com.example.eventplanner.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.eventplanner.R;
import com.example.eventplanner.databinding.FragmentCreateProductBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;
import com.example.eventplanner.fragments.home_screen_fragments.MyProductsFragment;
import com.example.eventplanner.viewmodel.CreateProductViewModel;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateProductFragment extends Fragment {

    private CreateProductViewModel viewModel;
    private FragmentCreateProductBinding binding; // View Binding
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    public GetAuthenticatedUserDTO user;

    public static CreateProductFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        CreateProductFragment fragment = new CreateProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        fragment.user = currentUser;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicijalizacija Activity Result Launchera za odabir slika
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        List<Uri> uris = new ArrayList<>();
                        if (result.getData().getClipData() != null) { // Više slika
                            ClipData clipData = result.getData().getClipData();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                uris.add(clipData.getItemAt(i).getUri());
                            }
                        } else if (result.getData().getData() != null) { // Jedna slika
                            uris.add(result.getData().getData());
                        }
                        viewModel.setSelectedImageUris(uris);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout-a koristeći View Binding
        binding = FragmentCreateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CreateProductViewModel.class);
        if (viewModel.currentUser == null) {
            GetSPProviderDTO providerDTO = new GetSPProviderDTO();
            providerDTO.setId(user.getId());
            viewModel.currentUser = providerDTO;
        }

        setupListeners();
        setupObservers();

        // Učitavanje inicijalnih podataka
        viewModel.loadCategories();
        // viewModel.loadCurrentUser(userId); // Pozovite sa ID-jem ulogovanog korisnika
    }

    private void setupListeners() {
        // Povezivanje unosa sa ViewModel-om
        binding.etName.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.name.setValue(text)));
        binding.etDescription.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.description.setValue(text)));
        binding.etPrice.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.price.setValue(text)));
        binding.etDiscount.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.discount.setValue(text)));
        binding.etProposedCategory.addTextChangedListener(new SimpleTextWatcher(text -> viewModel.proposedCategory.setValue(text)));

        binding.actvCategory.setOnItemClickListener((parent, view, position, id) -> {
            boolean isProposing = position == 0;
            viewModel.isProposingCategory.setValue(isProposing);
            if (!isProposing) {
                // Dobavljamo kategoriju na osnovu pozicije (moramo oduzeti 1 zbog "-- Propose new --")
                viewModel.selectedCategory.setValue(viewModel.getCategories().getValue().get(position - 1));
            } else {
                viewModel.selectedCategory.setValue(null);
            }
        });

        // Listener-i za dugmad
        binding.btnSelectImages.setOnClickListener(v -> openImagePicker());
        binding.btnSave.setOnClickListener(v -> viewModel.saveProduct());
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setupObservers() {
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        });

        viewModel.getCreationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                // Vrati se na prethodni ekran nakon uspešnog kreiranja
                getParentFragmentManager().popBackStack();
            }
        });

        viewModel.getCategories().observe(getViewLifecycleOwner(), this::updateCategoryDropdown);
        viewModel.isProposingCategory.observe(getViewLifecycleOwner(), isProposing -> {
            binding.layoutProposedCategory.setVisibility(isProposing ? View.VISIBLE : View.GONE);
        });

        updateCategorySelection();

        viewModel.getSelectedImageUris().observe(getViewLifecycleOwner(), this::updateImagePreviews);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Pictures"));
    }

    private void updateCategoryDropdown(List<GetCategoryDTO> categories) {
        if (categories == null) return;
        List<String> categoryNames = categories.stream().map(GetCategoryDTO::getName).collect(Collectors.toList());
        categoryNames.add(0, "-- Propose new category --");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        binding.actvCategory.setAdapter(adapter);
    }

    private void updateCategorySelection() {
        int categoryId = 0;
        if (viewModel.selectedCategory.getValue() != null) {
            categoryId = viewModel.selectedCategory.getValue().getId();
        }
        List<GetCategoryDTO> categories = viewModel.getCategories().getValue();

        // Izvrši logiku samo ako su i proizvod i kategorije učitani
        if (categoryId != 0 && categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).getId().equals(categoryId)) {
                    String categoryName = categories.get(i).getName();
                    binding.actvCategory.setText(categoryName, false); // false da ne bi otvarao listu
                    return;
                }
            }
        }
    }

    private void updateImagePreviews(List<Uri> uris) {
        binding.imageContainer.removeAllViews();
        if (uris == null || uris.isEmpty()) {
            // Ako nema slika, prikaži placeholder (koji je već u XML-u, ali ga možemo dodati i ovde za svaki slučaj)
            ImageView placeholder = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.image_preview_item, binding.imageContainer, false);
            placeholder.setImageResource(R.drawable.baseline_image_24);
            binding.imageContainer.addView(placeholder);
        } else {
            for (Uri uri : uris) {
                ImageView imageView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.image_preview_item, binding.imageContainer, false);
                imageView.setImageURI(uri);
                binding.imageContainer.addView(imageView);
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
        binding = null; // Obavezno za View Binding u fragmentima radi sprečavanja curenja memorije
    }
}