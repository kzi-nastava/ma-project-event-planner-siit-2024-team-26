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

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.IdentityHashMap;

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
    private MapView mapView;

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
                    setMap();
                }
            }

            @Override
            public void onFailure(Call<GetEventDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }

    private void setAttributes(){

    }

    private void setMap(){
        Configuration.getInstance().setUserAgentValue(requireContext().getOpPackageName());
        mapView = mainView.findViewById(R.id.eventMap);
        mapView.setMultiTouchControls(true);

        GeoPoint eventLocation = new GeoPoint(foundEvent.getAddress().getLocation().getLatitude(), foundEvent.getAddress().getLocation().getLongitude());

        //Map positioning
        IMapController mapController = mapView.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(eventLocation);

        // Drawing marker on the map
        Marker marker = new Marker(mapView);
        marker.setPosition(eventLocation);
        marker.setTitle("Event location!");
        mapView.getOverlays().add(marker);

        //Updating the map
        mapView.invalidate();
    }
}