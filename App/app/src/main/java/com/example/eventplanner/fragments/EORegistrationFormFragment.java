package com.example.eventplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventplanner.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EORegistrationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EORegistrationFormFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_PERMISSION = 1;
    TextView countryTextView;
    Button uploadButton;
    ImageView imageView;

    public EORegistrationFormFragment() {
        // Required empty public constructor
    }

    public static EORegistrationFormFragment newInstance() {
        EORegistrationFormFragment fragment = new EORegistrationFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eo_registration_form, container, false);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        countryTextView = view.findViewById(R.id.countryInput);
        imageView = view.findViewById(R.id.imageView3);
        uploadButton = view.findViewById(R.id.uploadButton);

        handleButtonClicks();
        // Inflate the layout for this fragment
        return view;
    }

    private void handleButtonClicks() {
        countryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] optionsArray = {"Serbia", "United States", "France", "Japan", "Romania"};
                int checkedItem = -1;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Country")
                        .setSingleChoiceItems(optionsArray, checkedItem, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                countryTextView.setHint(optionsArray[which]);
                                dialog.dismiss();

                            }
                        });

                builder.show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // For Android 13 (API 33) and above
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
                    } else {
                        // Permission already granted, open gallery
                        openGallery();
                    }
                } else {
                    // For Android versions below 13
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
                    } else {
                        // Permission already granted, open gallery
                        openGallery();
                    }
                }
            }
        });
    }


    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imageView.setImageURI(selectedImageUri);
            }
        }
    }
}