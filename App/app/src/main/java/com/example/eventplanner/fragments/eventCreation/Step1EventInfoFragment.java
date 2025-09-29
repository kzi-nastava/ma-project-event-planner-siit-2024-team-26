package com.example.eventplanner.fragments.eventCreation;
// Importi
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.eventType.GetEventTypeDTO;
import com.example.eventplanner.viewmodel.CreateEventViewModel;
import java.util.List;
import java.util.stream.Collectors;

public class Step1EventInfoFragment extends Fragment {
    private CreateEventViewModel viewModel;
    private AutoCompleteTextView actvType;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireParentFragment()).get(CreateEventViewModel.class);
        View view = inflater.inflate(R.layout.fragment_step1_event_info, container, false);

        EditText etName = view.findViewById(R.id.et_event_name);
        EditText etDesc = view.findViewById(R.id.et_event_description);
        EditText etMaxPart = view.findViewById(R.id.et_max_participants);
        RadioGroup rgPrivacy = view.findViewById(R.id.rg_privacy);
        actvType = view.findViewById(R.id.actv_event_type);
        progressBar = view.findViewById(R.id.progress_bar);

        // --- Povezivanje View-ova sa ViewModel-om ---

        // Name
        etName.setText(viewModel.name.getValue());
        etName.addTextChangedListener((SimpleTextWatcher) s -> viewModel.name.setValue(s.toString()));

        // Description
        etDesc.setText(viewModel.description.getValue());
        etDesc.addTextChangedListener((SimpleTextWatcher) s -> viewModel.description.setValue(s.toString()));

        // Max Participants
        etMaxPart.setText(viewModel.maxParticipants.getValue());
        etMaxPart.addTextChangedListener((SimpleTextWatcher) s -> viewModel.maxParticipants.setValue(s.toString()));

        // Privacy
        rgPrivacy.check(viewModel.privacyType.getValue().equals("OPEN") ? R.id.rb_open : R.id.rb_closed);
        rgPrivacy.setOnCheckedChangeListener((group, checkedId) -> {
            viewModel.privacyType.setValue(checkedId == R.id.rb_open ? "OPEN" : "CLOSED");
        });

        setupObservers();
        // Ako podaci još nisu učitani, pokreni učitavanje
        if (viewModel.eventTypes.getValue() == null) {
            viewModel.fetchEventTypes();
        }

        return view;
    }

    private void setupObservers() {
        // Posmatraj listu tipova događaja
        viewModel.eventTypes.observe(getViewLifecycleOwner(), types -> {
            if (types != null && !types.isEmpty()) {
                updateEventTypeDropdown(types);
            }
        });

        // Posmatraj stanje učitavanja
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            actvType.setEnabled(!isLoading); // Onemogući dropdown dok se učitava
        });

        // Posmatraj poruke o grešci
        viewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                viewModel.errorMessage.setValue(null); // Resetuj da se ne prikazuje ponovo
            }
        });
    }

    private void updateEventTypeDropdown(List<GetEventTypeDTO> types) {
        List<String> typeNames = types.stream().map(GetEventTypeDTO::getName).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, typeNames);
        actvType.setAdapter(adapter);

        // Ako je tip već bio selektovan, postavi ga ponovo
        if (viewModel.eventType.getValue() != null) {
            actvType.setText(viewModel.eventType.getValue().getName(), false);
        }

        actvType.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.eventType.setValue(types.get(position));
        });
    }

    // Helper interfejs za kraći kod
    @FunctionalInterface
    public interface SimpleTextWatcher extends TextWatcher {
        @Override default void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override default void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override void afterTextChanged(Editable s);
    }
}