package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventTypeAdapter;
import com.example.eventplanner.databinding.FragmentEventTypesBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.fragments.eventCreation.CreateEventHostFragment;
import com.example.eventplanner.viewmodel.EventTypeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class EventTypesFragment extends Fragment implements EventTypeAdapter.OnEventTypeActionClickListener {

    private EventTypeViewModel viewModel;
    private FragmentEventTypesBinding binding;
    private EventTypeAdapter adapter;

    private GetAuthenticatedUserDTO user;

    public static EventTypesFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        EventTypesFragment fragment = new EventTypesFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        fragment.user = currentUser;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEventTypesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EventTypeViewModel.class);

        setupRecyclerView();
        setupObservers();

        binding.btnAddEventType.setOnClickListener(v -> {
            // Navigacija na fragment za kreiranje novog tipa događaja
            Toast.makeText(getContext(), "Navigate to Add Event Type screen...", Toast.LENGTH_SHORT).show();
        });

        viewModel.fetchEventTypes();
    }

    private void setupRecyclerView() {
        adapter = new EventTypeAdapter(new ArrayList<>(), this);
        binding.eventTypesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.eventTypesRecyclerView.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), eventTypes -> {
            if (eventTypes != null) {
                adapter.updateEventTypes(eventTypes);
            }
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getSnackbarMessage().observe(getViewLifecycleOwner(), message -> {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        });
    }

    // --- Implementacija akcija sa kartice ---

    @Override
    public void onInfoClicked(GetEventTypeDTO eventType) {
        // 1. Kreiramo StringBuilder da bismo lako sastavili poruku
        StringBuilder messageBuilder = new StringBuilder();

        // 2. Dodajemo opis događaja
        messageBuilder.append(eventType.getDescription());

        // 3. Proveravamo da li postoje preporučene kategorije
        List<GetCategoryDTO> recommendedCategories = eventType.getRecommendedCategories();
        if (recommendedCategories != null && !recommendedCategories.isEmpty()) {
            // Dodajemo razmak, naslov i listu kategorija
            messageBuilder.append("\n\n"); // Dva nova reda za razmak
            messageBuilder.append("Recommended Categories:\n");

            // Prolazimo kroz listu i dodajemo imena kategorija
            for (int i = 0; i < recommendedCategories.size(); i++) {
                messageBuilder.append("• ").append(recommendedCategories.get(i).getName());
                if (i < recommendedCategories.size() - 1) {
                    messageBuilder.append("\n"); // Novi red za svaku kategoriju
                }
            }
        }

        // 4. Kreiramo dialog sa sastavljenom porukom
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(eventType.getName())
                .setMessage(messageBuilder.toString()) // Koristimo sastavljeni string
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onEditClicked(GetEventTypeDTO eventType) {
        // Navigacija na fragment za izmenu
        Toast.makeText(getContext(), "Navigate to Edit screen for: " + eventType.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToggleStatusClicked(GetEventTypeDTO eventType) {
        String action = eventType.isActive() ? "deactivate" : "activate";
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirm Action")
                .setMessage("Are you sure you want to " + action + " '" + eventType.getName() + "'?")
                .setPositiveButton("Yes", (dialog, which) -> viewModel.toggleActivationStatus(eventType))
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}