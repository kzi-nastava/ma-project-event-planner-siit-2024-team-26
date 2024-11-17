package com.example.eventplanner.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyInfoFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyInfoFormFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSION = 1;
    TextView countryTextView;
    Button uploadButton;
    TextView imagesString;

    public CompanyInfoFormFragment() {
        // Required empty public constructor
    }


    public static CompanyInfoFormFragment newInstance() {
        CompanyInfoFormFragment fragment = new CompanyInfoFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_info_form, container, false);
        Button returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        countryTextView = view.findViewById(R.id.countryInput);
        uploadButton = view.findViewById(R.id.uploadButton);
        imagesString = view.findViewById(R.id.images_added);

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

        uploadButton.setOnClickListener(v -> {
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
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple images
        startActivityForResult(intent, 100); // Start the gallery activity
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                // Multiple images selected
                int imageCount = data.getClipData().getItemCount();
                if (imageCount > 1) {
                    imagesString.setText("You selected " + imageCount + " images");
                } else {
                    imagesString.setText("You selected 1 image");
                }
            } else if (data.getData() != null) {
                // Single image selected
                imagesString.setText("You selected 1 image");
            }
        }
    }
}