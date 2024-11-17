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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eventplanner.HomeActivity;
import com.example.eventplanner.R;
import com.example.eventplanner.fragments.home_screen_fragments.HomeScreenFragment;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceCreationFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceCreationFormFragment extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 1;

    CheckBox recommendNewCategoryCheckBox;
    CheckBox fixedServiceDurationCheckBox;
    EditText newCategoryEditText;
    EditText newCategoryDescriptionEditText;
    EditText minServiceEngagementEditText;
    EditText maxServiceEngagementEditText;
    EditText serviceDurationEditText;
    TextView categoryTextView;
    TextView eventTextView;
    TextView imagesString;

    Button uploadButton;

    public ServiceCreationFormFragment() {
        // Required empty public constructor
    }

    public static ServiceCreationFormFragment newInstance() {
        ServiceCreationFormFragment fragment = new ServiceCreationFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_creation_form, container, false);
        recommendNewCategoryCheckBox = view.findViewById(R.id.recommendNewCategoryCheckBox);
        fixedServiceDurationCheckBox = view.findViewById(R.id.fixedServiceDurationCheckBox);
        newCategoryEditText = view.findViewById(R.id.newCategory);
        newCategoryDescriptionEditText = view.findViewById(R.id.newCategoryDescription);
        minServiceEngagementEditText = view.findViewById(R.id.minEngagement);
        maxServiceEngagementEditText = view.findViewById(R.id.maxEngagement);
        serviceDurationEditText = view.findViewById(R.id.serviceDuration);
        categoryTextView = view.findViewById(R.id.category);
        eventTextView = view.findViewById(R.id.eventType);
        uploadButton = view.findViewById(R.id.serviceImagesButton);
        imagesString = view.findViewById(R.id.imagesAdded);

        checkIsDisabledDurationInput();
        checkIsDisabledCategoriesInputs();
        handleButtonClicks();
        return view;
    }

    private void handleButtonClicks(){
        recommendNewCategoryCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsDisabledCategoriesInputs();
            }
        });
        fixedServiceDurationCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsDisabledDurationInput();
            }
        });

        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!recommendNewCategoryCheckBox.isChecked()){
                    String[] optionsArray = {"Music", "Decoration", "Entertaining"};
                    int checkedItem = -1;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Category")
                            .setSingleChoiceItems(optionsArray, checkedItem, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    categoryTextView.setHint(optionsArray[which]);
                                    dialog.dismiss();

                                }
                            });


                    builder.show();
                }
            }
        });
        boolean[] checkedItems = {false, false, false};
        eventTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] optionsArray = {"Wedding", "Conference", "Birthday"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Event types")
                        .setMultiChoiceItems(optionsArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple images
        startActivityForResult(intent, 100); // Start the gallery activity
    }

    private void checkIsDisabledCategoriesInputs(){
        if (recommendNewCategoryCheckBox.isChecked()){
            newCategoryEditText.setEnabled(true);
            newCategoryDescriptionEditText.setEnabled(true);
            newCategoryEditText.setBackgroundResource(R.color.white);
            newCategoryDescriptionEditText.setBackgroundResource(R.color.white);
            categoryTextView.setBackgroundResource(R.color.neutral);
            categoryTextView.setHintTextColor(getResources().getColor(R. color. neutral));
        }else{
            newCategoryEditText.setEnabled(false);
            newCategoryDescriptionEditText.setEnabled(false);
            newCategoryEditText.setBackgroundResource(R.color.neutral);
            newCategoryDescriptionEditText.setBackgroundResource(R.color.neutral);
            categoryTextView.setBackgroundResource(R.color.accent);
            categoryTextView.setHintTextColor(getResources().getColor(R. color. black));
            categoryTextView.setText("");
            newCategoryEditText.setText("");
        }
    }

    private void checkIsDisabledDurationInput(){
        if (fixedServiceDurationCheckBox.isChecked()){
            minServiceEngagementEditText.setEnabled(false);
            maxServiceEngagementEditText.setEnabled(false);
            serviceDurationEditText.setEnabled(true);
            minServiceEngagementEditText.setBackgroundResource(R.color.neutral);
            maxServiceEngagementEditText.setBackgroundResource(R.color.neutral);
            serviceDurationEditText.setBackgroundResource(R.color.white);
            minServiceEngagementEditText.setText("");
            maxServiceEngagementEditText.setText("");

        }else{
            minServiceEngagementEditText.setEnabled(true);
            maxServiceEngagementEditText.setEnabled(true);
            serviceDurationEditText.setEnabled(false);
            minServiceEngagementEditText.setBackgroundResource(R.color.white);
            maxServiceEngagementEditText.setBackgroundResource(R.color.white);
            serviceDurationEditText.setBackgroundResource(R.color.neutral);
            serviceDurationEditText.setText("");
        }
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