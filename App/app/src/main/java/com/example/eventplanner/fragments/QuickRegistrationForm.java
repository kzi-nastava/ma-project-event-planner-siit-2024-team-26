package com.example.eventplanner.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickRegistrationForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickRegistrationForm extends Fragment {

    String givenEmail;
    TextView givenEmailTextView;

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
        return view;
    }
}