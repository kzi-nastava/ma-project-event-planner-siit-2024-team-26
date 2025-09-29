package com.example.eventplanner.fragments.eventCreation;
// Importi
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.eventplanner.R;
import com.example.eventplanner.viewmodel.CreateEventViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Step2LocationTimeFragment extends Fragment {

    private CreateEventViewModel viewModel;
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

    private AutoCompleteTextView actvCountry;
    private EditText etCity, etStreet, etNumber, etStartDate, etEndDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireParentFragment()).get(CreateEventViewModel.class);
        return inflater.inflate(R.layout.fragment_step2_location_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Povezivanje UI elemenata
        actvCountry = view.findViewById(R.id.actv_country);
        etCity = view.findViewById(R.id.et_city);
        etStreet = view.findViewById(R.id.et_street);
        etNumber = view.findViewById(R.id.et_number);
        etStartDate = view.findViewById(R.id.et_start_date);
        etEndDate = view.findViewById(R.id.et_end_date);

        // Postavljanje listener-a koji upisuju podatke u ViewModel
        setupListeners();

        // Postavljanje observer-a koji čitaju podatke iz ViewModel-a i popunjavaju UI
        setupObservers();
    }

    private void setupListeners() {
        // --- Listener-i za unos teksta ---
        actvCountry.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.country.setValue((String) parent.getItemAtPosition(position));
        });
        etCity.addTextChangedListener(new SimpleTextWatcher(s -> viewModel.city.setValue(s)));
        etStreet.addTextChangedListener(new SimpleTextWatcher(s -> viewModel.street.setValue(s)));
        etNumber.addTextChangedListener(new SimpleTextWatcher(s -> viewModel.number.setValue(s)));

        // --- Listener-i za klik na polja za datum ---
        etStartDate.setOnClickListener(v -> showDateTimePicker(true));
        etEndDate.setOnClickListener(v -> showDateTimePicker(false));
    }

    private void setupObservers() {
        // --- Observer-i koji popunjavaju polja ---
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, viewModel.getMockCountries());
        actvCountry.setAdapter(countryAdapter);

        // Posmatraj vrednosti iz ViewModel-a i ažuriraj UI ako se razlikuju
        viewModel.country.observe(getViewLifecycleOwner(), value -> {
            if (!actvCountry.getText().toString().equals(value)) actvCountry.setText(value, false);
        });
        viewModel.city.observe(getViewLifecycleOwner(), value -> {
            if (!etCity.getText().toString().equals(value)) etCity.setText(value);
        });
        viewModel.street.observe(getViewLifecycleOwner(), value -> {
            if (!etStreet.getText().toString().equals(value)) etStreet.setText(value);
        });
        viewModel.number.observe(getViewLifecycleOwner(), value -> {
            if (!etNumber.getText().toString().equals(value)) etNumber.setText(value);
        });

        // POSEBNO VAŽNO: Observer-i za datume
        viewModel.startDate.observe(getViewLifecycleOwner(), calendar -> {
            if (calendar != null) {
                etStartDate.setText(dateTimeFormat.format(calendar.getTime()));
            } else {
                etStartDate.setText("");
            }
        });
        viewModel.endDate.observe(getViewLifecycleOwner(), calendar -> {
            if (calendar != null) {
                etEndDate.setText(dateTimeFormat.format(calendar.getTime()));
            } else {
                etEndDate.setText("");
            }
        });
    }

    private void showDateTimePicker(boolean isStart) {
        Calendar currentCalendar = isStart ? viewModel.startDate.getValue() : viewModel.endDate.getValue();
        if (currentCalendar == null) {
            currentCalendar = Calendar.getInstance();
        }

        final Calendar selectedCalendar = (Calendar) currentCalendar.clone();

        new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, month);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, day);

            new TimePickerDialog(requireContext(), (timePicker, hour, minute) -> {
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hour);
                selectedCalendar.set(Calendar.MINUTE, minute);

                // OVDE SE RADI ISPRAVKA - AŽURIRAMO SAMO VIEWMODEL
                // Observer će se pobrinuti da ažurira UI (EditText)
                if (isStart) {
                    viewModel.startDate.setValue(selectedCalendar);
                } else {
                    viewModel.endDate.setValue(selectedCalendar);
                }
            }, selectedCalendar.get(Calendar.HOUR_OF_DAY), selectedCalendar.get(Calendar.MINUTE), true).show();

        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Helper klasa za kraći kod TextWatcher-a
    private static class SimpleTextWatcher implements TextWatcher {
        private final OnTextChanged listener;
        public SimpleTextWatcher(OnTextChanged listener) { this.listener = listener; }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { listener.onTextChanged(s.toString()); }
        interface OnTextChanged { void onTextChanged(String text); }
    }
}