package com.example.eventplanner.fragments.eventCreation;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.EventReservationDTO;
import com.example.eventplanner.dto.invitation.CreateInvitationDTO;
import com.example.eventplanner.dto.invitation.CreatedInvitationDTO;
import com.example.eventplanner.viewmodel.CreateEventViewModel;

import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step3InvitationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step3InvitationsFragment extends Fragment {

    Button addButton;
    Button resetButton;
    TextView emailTextView;
    TextView enteredEmailsTextView;

    ArrayList<String> enteredEmails;

    private CreateEventViewModel viewModel;


    public Step3InvitationsFragment() {
        // Required empty public constructor
    }

    public static Step3InvitationsFragment newInstance(String param1, String param2) {
        Step3InvitationsFragment fragment = new Step3InvitationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        enteredEmails = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation_form, container, false);
        addButton = view.findViewById(R.id.addButton);
        resetButton = view.findViewById(R.id.resetButton);
        emailTextView = view.findViewById(R.id.emailInput);
        enteredEmailsTextView = view.findViewById(R.id.enteredEmails);
        enteredEmailsTextView.setMovementMethod(new ScrollingMovementMethod());
        viewModel = new ViewModelProvider(requireParentFragment()).get(CreateEventViewModel.class);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                if (isValidEmail(email)){
                    if (!viewModel.getEmailsList().contains(email)) {
                        viewModel.addEmailToEmailsList(email);
                    }
                    enteredEmailsTextView.setText(makeEmailsString());
                    emailTextView.setHint("");
                }else{
                    if (email.equals("") || email == null){
                        emailTextView.setHint("You have to enter email!");
                    }
                    else{
                        emailTextView.setHint("Format is not valid!");
                    }
                    emailTextView.setHintTextColor(ContextCompat.getColor(getContext(), R.color.primary));

                }
                emailTextView.setText("");
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                enteredEmails.clear();
                viewModel.clearEmailsList();
                enteredEmailsTextView.setText(makeEmailsString());
            }
        });

        return view;
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    private String makeEmailsString(){
        return String.join("\n", viewModel.getEmailsList());
    }

//    private CreateInvitationDTO setUpinvitationDTO(String email){
//        EventReservationDTO event = new EventReservationDTO(3, "Test name", "Test description", "2025-10-12T13:00");
//        return new CreateInvitationDTO(email, event, "Come to our closed type event!" );
//    }


//    private void sendInvitation(String email){
//        CreateInvitationDTO invitation = setUpinvitationDTO(email);
//        Call<CreatedInvitationDTO> call = ClientUtils.invitationService.createInvitation(invitation);
//        call.enqueue(new Callback<CreatedInvitationDTO>() {
//
//            @Override
//            public void onResponse(Call<CreatedInvitationDTO> call, Response<CreatedInvitationDTO> response) {
//                if (response.isSuccessful()) {
//                }
//            }
//            @Override
//            public void onFailure(Call<CreatedInvitationDTO> call, Throwable t) {
//                Log.i("POZIV", t.getMessage());
//            }
//        });
//    }
}