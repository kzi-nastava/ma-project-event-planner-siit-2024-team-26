package com.example.eventplanner.fragments.details;

import android.os.Bundle;
import android.util.Log;
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
import com.example.eventplanner.databinding.FragmentUserDetailsBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsFragment extends Fragment {

    private static final String ARG_USER_ID = "userId";
    private Integer userId;
    private GetAuthenticatedUserDTO foundUser;
    private FragmentUserDetailsBinding binding;
    private boolean isExpanded = false;

    public static UserDetailsFragment newInstance(Integer userId) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false);
        getUserDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    }

    private void getUserDetails() {
        ClientUtils.authenticatedUserService.getUserByID(userId).enqueue(new Callback<GetAuthenticatedUserDTO>() {
            @Override
            public void onResponse(Call<GetAuthenticatedUserDTO> call, Response<GetAuthenticatedUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foundUser = response.body();
                    setAttributes();
                } else {
                    Toast.makeText(getContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetAuthenticatedUserDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UserDetails", "API error", t);
            }
        });
    }

    private void setAttributes() {
        String fullName = foundUser.getFirstName() + " " + foundUser.getLastName();
        binding.userFullName.setText(fullName);

        String role = "User";
        String foundRole = foundUser.getRole().toString();
        if (foundRole.equals("ADMINISTRATOR")) {
            role = "Administrator";
        } else if (foundRole.equals("SERVICE_PRODUCT_PROVIDER")) {
            role = "Service & product provider";
        } else if (foundRole.equals("EVENT_ORGANIZER")) {
            role = "Event organizer";
        }

        binding.roleText.setText(role);
        binding.phoneText.setText(foundUser.getPhoneNumber());
        binding.emailText.setText(foundUser.getEmail());

        if (foundUser.getAddress() != null) {
            binding.addressText2.setText(foundUser.getAddress().getStreet() + " " + foundUser.getAddress().getNumber());
            binding.addressText1.setText(foundUser.getAddress().getCountry() + ", " + foundUser.getAddress().getCity());
        } else {
            binding.addressText1.setText("No address provided");
            binding.addressText2.setText("");
        }

        String imageUrl = foundUser.getImage(); // primer: https://mojserver.com/slike/user123.jpg

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
        binding.fabChatUser.setVisibility(View.VISIBLE);
        binding.textChatUser.setVisibility(View.VISIBLE);
        binding.fabReport.setVisibility(View.VISIBLE);
        binding.textFavourites.setVisibility(View.VISIBLE);
        isExpanded = true;
    }

    private void shrinkFab() {
        binding.transparentBg.setVisibility(View.GONE);
        binding.fabMain.animate().rotation(0f).start();
        binding.fabChatUser.setVisibility(View.INVISIBLE);
        binding.textChatUser.setVisibility(View.INVISIBLE);
        binding.fabReport.setVisibility(View.INVISIBLE);
        binding.textFavourites.setVisibility(View.INVISIBLE);
        isExpanded = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
