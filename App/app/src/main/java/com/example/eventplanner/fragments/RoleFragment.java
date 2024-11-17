package com.example.eventplanner.fragments;
import androidx.activity.OnBackPressedCallback;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.eventplanner.HomeActivity;
import com.example.eventplanner.LoginActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public RoleFragment() {
        // Required empty public constructor
    }

    public static RoleFragment newInstance() {
        RoleFragment fragment = new RoleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleBackButtonClicked();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role, container, false);
        // Inflate the layout for this fragment
        ImageView sppImage = view.findViewById(R.id.SPPImage);
        sppImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(SPPRegistrationFormFragment.newInstance(), getActivity(), true, R.id.containerRegister);

            }
        });

        ImageView eoImage = view.findViewById(R.id.EventOrganizerImage);
        eoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransition.to(EORegistrationFormFragment.newInstance(),getActivity(),true, R.id.containerRegister);
            }
        });

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;

    }

    private void handleBackButtonClicked(){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


}