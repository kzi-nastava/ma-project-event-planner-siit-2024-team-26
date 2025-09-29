package com.example.eventplanner.fragments.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.authorization.TokenManager;
import com.example.eventplanner.databinding.FragmentUserDetailsBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoggedDetailsFragment extends Fragment {

    private FragmentUserDetailsBinding binding;
    private TokenManager tokenManager;
    private boolean isExpanded = false;

    public UserLoggedDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenManager = new TokenManager(requireContext());

        String email = tokenManager.getEmail(tokenManager.getToken());
        if (email == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        ClientUtils.authenticatedUserService.getUserByEmail(email).enqueue(new Callback<GetAuthenticatedUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetAuthenticatedUserDTO> call, @NonNull Response<GetAuthenticatedUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetAuthenticatedUserDTO user = response.body();
                    setAttributes(user);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAuthenticatedUserDTO> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.fabMain.setOnClickListener(v -> {
            if (isExpanded) shrinkFab(); else expandFab();
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isExpanded) {
                    shrinkFab();
                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });

        binding.fabReport.setOnClickListener(v ->
                Toast.makeText(getContext(), "Edit profile clicked", Toast.LENGTH_SHORT).show());

        binding.textFavourites.setOnClickListener(v ->
                Toast.makeText(getContext(), "Edit profile clicked", Toast.LENGTH_SHORT).show());

        binding.fabChatUser.setOnClickListener(v ->
                Toast.makeText(getContext(), "Change password clicked", Toast.LENGTH_SHORT).show());

        binding.textChatUser.setOnClickListener(v ->
                Toast.makeText(getContext(), "Change password clicked", Toast.LENGTH_SHORT).show());

        binding.fabChatUser.setVisibility(View.GONE);
        binding.textChatUser.setVisibility(View.GONE);
        binding.textFavourites.setText("Edit Profile");
        binding.textChatUser.setText("Change password");
        binding.fabChatUser.setImageResource(R.drawable.baseline_settings_24);
        binding.fabReport.setImageResource(R.drawable.baseline_edit_24);
    }

    private void setAttributes(GetAuthenticatedUserDTO user) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        binding.userFullName.setText(fullName);
        binding.emailText.setText(user.getEmail());
        binding.phoneText.setText(user.getPhoneNumber());

        String role = "User";
        String foundRole = user.getRole().toString();
        if (foundRole.equals("ADMINISTRATOR")) {
            role = "Administrator";
        } else if (foundRole.equals("SERVICE_PRODUCT_PROVIDER")) {
            role = "Service & product provider";
        } else if (foundRole.equals("EVENT_ORGANIZER")) {
            role = "Event organizer";
        }

        binding.roleText.setText(role);

        if (user.getAddress() != null) {
            binding.addressText1.setText(user.getAddress().getCountry() + ", " + user.getAddress().getCity());
            binding.addressText2.setText(user.getAddress().getStreet() + " " + user.getAddress().getNumber());
        } else {
            binding.addressText1.setText("No address provided");
            binding.addressText2.setText("");
        }

        String imageUrl = user.getImage(); // primer: https://mojserver.com/slike/user123.jpg

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24) // fallback ako slika još nije učitana
                    .error(R.drawable.baseline_person_24)           // fallback ako slika ne postoji
                    .into(binding.profileImage);
        }

    }

    private void expandFab() {
        binding.transparentBg.setVisibility(View.VISIBLE);
        binding.fabMain.animate().rotation(45f).start();
        binding.fabReport.setVisibility(View.VISIBLE);
        binding.fabChatUser.setVisibility(View.VISIBLE);
        binding.textFavourites.setVisibility(View.VISIBLE);
        binding.textChatUser.setVisibility(View.VISIBLE);
        isExpanded = true;
    }

    private void shrinkFab() {
        binding.transparentBg.setVisibility(View.GONE);
        binding.fabMain.animate().rotation(0f).start();
        binding.fabReport.setVisibility(View.GONE);
        binding.fabChatUser.setVisibility(View.GONE);
        binding.textFavourites.setVisibility(View.GONE);
        binding.textChatUser.setVisibility(View.GONE);
        isExpanded = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
