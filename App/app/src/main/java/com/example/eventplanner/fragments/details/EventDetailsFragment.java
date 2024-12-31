package com.example.eventplanner.fragments.details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.event.GetEventDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {


    private Integer id;

    private GetEventDTO foundEvent;

    private View mainView;

    public EventDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventDetailsFragment newInstance(Integer eventId) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            id = args.getInt("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        mainView = view;
        getEventDetails();
        return view;
    }

    private void getEventDetails(){
        Call<GetEventDTO> call = ClientUtils.eventService.getById(id);
        call.enqueue(new Callback<GetEventDTO>() {

            @Override
            public void onResponse(Call<GetEventDTO> call, Response<GetEventDTO> response) {
                if (response.isSuccessful()) {
                    foundEvent = response.body();
                    setAttributes();
                }
            }

            @Override
            public void onFailure(Call<GetEventDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setAttributes(){
        TextView eventNameTextView = mainView.findViewById(R.id.eventName);
        eventNameTextView.setText(foundEvent.getName());
    }
}