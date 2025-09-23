package com.example.eventplanner.fragments.eventCreation;
// Svi potrebni importi
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.fragments.home_screen_fragments.NotificationsFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventCreationPagerAdapter;
import com.example.eventplanner.viewmodel.CreateEventViewModel;

public class CreateEventHostFragment extends Fragment {

    private CreateEventViewModel viewModel;
    private ViewPager2 viewPager;
    private Button btnBack, btnNextSave;
    private TabLayout tabLayout;
    public GetAuthenticatedUserDTO user;

    private final String[] stepTitles = {"Info", "Location", "Invites", "Agenda", "Budget"};

    public static CreateEventHostFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        CreateEventHostFragment fragment = new CreateEventHostFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        fragment.user = currentUser;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_event_host, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Scoped ViewModel to the fragment
        viewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);
        if (viewModel.currentUser == null) {
            viewModel.setCurrentUser(user);
        }

        //Initialize email list for event invitations
        viewModel.instanciateEmailsList();

        // Find views
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout_stepper);
        btnBack = view.findViewById(R.id.btn_back);
        btnNextSave = view.findViewById(R.id.btn_next_save);

        // Setup ViewPager
        viewPager.setAdapter(new EventCreationPagerAdapter(this));
        viewPager.setUserInputEnabled(false); // Disable swipe

        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(stepTitles[position])).attach();

        setupListeners();
        setupObservers();
    }

    private void setupListeners() {
        btnNextSave.setOnClickListener(v -> handleNextClick());
        btnBack.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateButtonState(position);
            }
        });
    }

    private void setupObservers() {
        viewModel.snackbarMessage.observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                showSnackbar(message);
                viewModel.snackbarMessage.setValue(null); // Reset after showing
            }
        });

        viewModel.eventCreationSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if(isSuccess) {
                // Navigate away or show success screen
                btnNextSave.setEnabled(false);
                btnBack.setEnabled(false);
            }
        });
    }

    private void handleNextClick() {
        int currentStep = viewPager.getCurrentItem();
        boolean isValid = true;

        if (currentStep == 0) {
            isValid = viewModel.isStep1Valid();
            if (!isValid) showSnackbar("Please fill all fields in Step 1.");
        } else if (currentStep == 1) {
            isValid = viewModel.isStep2Valid();
            if (!isValid) showSnackbar("Please check location and date fields.");
        }

        if (isValid) {
            if (currentStep < 4) { // 4 is the last index
                viewPager.setCurrentItem(currentStep + 1, true);
            } else {
                viewModel.submitEvent();
            }
        }
    }

    private void updateButtonState(int position) {
        btnBack.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
        if (position == 4) {
            btnNextSave.setText("Save Event");
            // Menjamo stil dugmeta programski
            btnNextSave.getBackground().setTint(getResources().getColor(R.color.accent));
            btnNextSave.setTextColor(getResources().getColor(R.color.black));
        } else {
            btnNextSave.setText("Next");
            btnNextSave.getBackground().setTint(getResources().getColor(R.color.primary));
            btnNextSave.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }
}