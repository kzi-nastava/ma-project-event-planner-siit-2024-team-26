package com.example.eventplanner.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.LoginActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ServiceSearchAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.CreateAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.CreatedAuthenticatedUserDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.model.Page;
import com.example.eventplanner.model.Role;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickRegistrationForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickRegistrationForm extends Fragment {

    String givenEmail;
    TextView givenEmailTextView;
    TextView givenPasswordTextView;
    Button registerButton;

    View mainView;

    public QuickRegistrationForm() {
        // Required empty public constructor
    }


    public static QuickRegistrationForm newInstance(String email) {
        QuickRegistrationForm fragment = new QuickRegistrationForm();
        Bundle args = new Bundle();
        args.putString("givenEmail", email);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            givenEmail = getArguments().getString("givenEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quick_registration_form, container, false);

        givenEmailTextView = view.findViewById(R.id.givenEmail);
        givenEmailTextView.setText(givenEmail);

        givenPasswordTextView = view.findViewById(R.id.givenPassword);
        givenPasswordTextView.setText(UUID.randomUUID().toString().replace("-", "").substring(0, 15));

        registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        mainView = view;
        return view;
    }

    private void addUser(){
        CreateAuthenticatedUserDTO userToRegister = setUpUser();


        Call<CreatedAuthenticatedUserDTO> call = ClientUtils.authenticatedUserService.createAuthenticatedUser(userToRegister);
        call.enqueue(new Callback<CreatedAuthenticatedUserDTO>() {

            @Override
            public void onResponse(Call<CreatedAuthenticatedUserDTO> call, Response<CreatedAuthenticatedUserDTO> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<CreatedAuthenticatedUserDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private CreateAuthenticatedUserDTO setUpUser(){
        CreateAuthenticatedUserDTO userToRegister = new CreateAuthenticatedUserDTO();
        userToRegister.setEmail(givenEmail);
        userToRegister.setPassword(givenPasswordTextView.getText().toString());
        TextView firstNameTextView = mainView.findViewById(R.id.firstNameInput);
        userToRegister.setFirstName(firstNameTextView.getText().toString());
        TextView lastNameTextView = mainView.findViewById(R.id.lastNameInput);
        userToRegister.setLastName(lastNameTextView.getText().toString());
        userToRegister.setAddress(null);
        userToRegister.setImage(null);
        userToRegister.setPhoneNumber(null);
        userToRegister.setRole(Role.AUTHENTICATED_USER);
        userToRegister.setActive(true);
        return userToRegister;
    }
}