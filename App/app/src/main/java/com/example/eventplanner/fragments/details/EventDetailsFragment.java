package com.example.eventplanner.fragments.details;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;
import android.graphics.Rect;


import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.CreateChatDTO;
import com.example.eventplanner.dto.chat.CreatedChatDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.GetEventDTO;

import com.example.eventplanner.databinding.FragmentEventDetailsBinding;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.home_screen_fragments.ChatTabFragment;
import com.example.eventplanner.fragments.home_screen_fragments.SingleChatFragment;
import com.example.eventplanner.utils.DateStringFormatter;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.time.format.DateTimeFormatter;

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
    private GetAuthenticatedUserDTO currentUser;

    private GetChatDTO chat;

    private View mainView;
    private MapView mapView;

    private FragmentEventDetailsBinding binding;
    private boolean isExpanded = false;

    private Animation fromBottomFabAnim;
    private Animation toBottomFabAnim;
    private Animation rotateClockWiseFabAnim;
    private Animation rotateAntiClockWiseFabAnim;
    private Animation fromBottomBgAnim;
    private Animation toBottomBgAnim;


    public EventDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventDetailsFragment newInstance(Integer eventId, GetChatDTO chat, GetAuthenticatedUserDTO currentUser) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("eventId", eventId);
        args.putParcelable("chat", chat);
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            id = args.getInt("eventId");
            chat = args.getParcelable("chat");
            currentUser = args.getParcelable("currentUser");
        }
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        mainView = view;
        getEventDetails();
        return view;
    }*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false);
        getEventDetails();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Učitaj animacije
        fromBottomFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_fab);
        toBottomFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_fab);
        rotateClockWiseFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_clock_wise);
        rotateAntiClockWiseFabAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anti_clock_wise);
        fromBottomBgAnim = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottomBgAnim = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        binding.fabMain.setOnClickListener(v -> {
            if (isExpanded) {
                shrinkFab();
            } else {
                expandFab();
            }
        });

        binding.fabFavourites.setOnClickListener(v -> onFavouritesClicked());
        binding.textFavourites.setOnClickListener(v -> onFavouritesClicked());

        binding.fabGoing.setOnClickListener(v -> onGoingClicked());
        binding.textGoing.setOnClickListener(v -> onGoingClicked());

        binding.fabDownload.setOnClickListener(v -> onDownloadClicked());
        binding.textDownload.setOnClickListener(v -> onDownloadClicked());

        binding.fabOrganizer.setOnClickListener(v -> onOrganizerClicked());
        binding.textOrganizer.setOnClickListener(v -> onOrganizerClicked());

        binding.fabChat.setOnClickListener(v -> onChatClicked());
        binding.textChat.setOnClickListener(v -> onChatClicked());

        View[] views = {
                binding.fabDownload, binding.fabGoing, binding.fabFavourites,
                binding.fabChat, binding.fabOrganizer,
                binding.textDownload, binding.textGoing, binding.textFavourites,
                binding.textChat, binding.textOrganizer
        };

        for (View v : views) {
            v.setClickable(false);
        }

        // Ostali click listeneri po potrebi

        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isExpanded) {
                            // "Simuliramo" klik van FAB-a (kao u onTouchListener)
                            shrinkFab();
                        } else {
                            setEnabled(false); // isključi callback, pa prosledi sistemu
                            requireActivity().onBackPressed();
                        }
                    }
                }
        );

    }

    private void onFavouritesClicked() {
        Toast.makeText(getContext(), "Favourites Clicked", Toast.LENGTH_SHORT).show();
    }

    private void onGoingClicked() {
        Toast.makeText(getContext(), "Going Clicked", Toast.LENGTH_SHORT).show();
    }

    private void onDownloadClicked() {
        Toast.makeText(getContext(), "Download Clicked", Toast.LENGTH_SHORT).show();
    }

    private void onOrganizerClicked() {
        Toast.makeText(getContext(), "Organizer Clicked", Toast.LENGTH_SHORT).show();
    }

    private void onChatClicked() {
        ChatAuthenticatedUserDTO otherUser = null;
        boolean isAuthenticatedUser = false;
        if (currentUser == null){
            Toast.makeText(getContext(), "No permission to chat with event organizer!", Toast.LENGTH_SHORT).show();
        }
        else if (chat != null){
            removeAllFromBackStack();
            if (chat.getEventOrganizer().getId() == currentUser.getId()){
                otherUser = chat.getAuthenticatedUser();
                isAuthenticatedUser = false;
            }
            else{
                otherUser = chat.getEventOrganizer();
                isAuthenticatedUser = true;
            }

            FragmentTransition.to(ChatTabFragment.newInstance(currentUser), getActivity(), false, R.id.mainScreenFragment);
            FragmentTransition.to(SingleChatFragment.newInstance(currentUser, otherUser, isAuthenticatedUser), getActivity(), true, R.id.mainScreenFragment);
        }
        else if (chat == null){
            if (foundEvent.getEventOrganizer().getId() == currentUser.getId()){
                Toast.makeText(getContext(), "You can't start chat with yourself!", Toast.LENGTH_SHORT).show();
            }else{
                startNewChat();
            }
        }

    }

    private void shrinkFab() {
        binding.transparentBg.startAnimation(toBottomBgAnim);
        binding.transparentBg.setVisibility(View.INVISIBLE);
        binding.fabMain.startAnimation(rotateAntiClockWiseFabAnim);
        View[] views = {
                binding.fabDownload, binding.fabGoing, binding.fabFavourites,
                binding.fabChat, binding.fabOrganizer,
                binding.textDownload, binding.textGoing, binding.textFavourites,
                binding.textChat, binding.textOrganizer
        };

        for (View view : views) {
            view.startAnimation(toBottomFabAnim);
            view.setVisibility(View.GONE);
            view.setClickable(false);
        }
        isExpanded = false;
    }

    private void expandFab() {
        binding.transparentBg.bringToFront();
        binding.fabMain.bringToFront();
        binding.fabDownload.bringToFront();
        binding.fabGoing.bringToFront();
        binding.fabFavourites.bringToFront();
        binding.fabChat.bringToFront();
        binding.fabOrganizer.bringToFront();
        binding.textDownload.bringToFront();
        binding.textGoing.bringToFront();
        binding.textFavourites.bringToFront();
        binding.textChat.bringToFront();
        binding.textOrganizer.bringToFront();
        binding.transparentBg.startAnimation(fromBottomBgAnim);
        binding.transparentBg.setVisibility(View.VISIBLE);
        binding.fabMain.startAnimation(rotateClockWiseFabAnim);
        View[] views = {
                binding.fabDownload, binding.fabGoing, binding.fabFavourites,
                binding.fabChat, binding.fabOrganizer,
                binding.textDownload, binding.textGoing, binding.textFavourites,
                binding.textChat, binding.textOrganizer
        };

        for (View view : views) {
            view.startAnimation(fromBottomFabAnim);
            view.setVisibility(View.VISIBLE);
            view.setClickable(true);
        }
        isExpanded = true;
    }

    // Ako želiš da hvataš dodire izvan FAB-a
    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isExpanded) {
                    Rect outRect = new Rect();
                    binding.constraintLayout.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        v.performClick(); // Dodajemo zbog accessibility zahteva
                        shrinkFab();
                    }
                }
                return false; // vrati false da event ide dalje ako treba
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getEventDetails() {
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

    private void setAttributes() {
        binding.eventTitle.setText(foundEvent.getName());
        binding.description.setText(foundEvent.getDescription());
        binding.categoryText.setText(foundEvent.getEventType().getName());
        binding.privacyText.setText(foundEvent.getPrivacyType().toString());
        String eoName = foundEvent.getEventOrganizer().getFirstName() + " " + foundEvent.getEventOrganizer().getLastName();
        binding.eoText.setText(eoName);
        String address1 = foundEvent.getAddress().getCountry() + ", " + foundEvent.getAddress().getCity();
        binding.addressText1.setText(address1);
        String address2 = foundEvent.getAddress().getStreet() + ", " + foundEvent.getAddress().getNumber();
        binding.addressText2.setText(address2);
        binding.date2Text.setText(DateStringFormatter.format(foundEvent.getStarts(), "dd.MM.yyyy HH:mm"));
        binding.date3Text.setText(DateStringFormatter.format(foundEvent.getEnds(), "dd.MM.yyyy HH:mm"));
    }

    private void setMap() {
        Configuration.getInstance().setUserAgentValue(requireContext().getOpPackageName());
        mapView = binding.eventMap;
        mapView.setMultiTouchControls(true);

        GeoPoint eventLocation = new GeoPoint(
                foundEvent.getAddress().getLocation().getLatitude(),
                foundEvent.getAddress().getLocation().getLongitude()
        );

        IMapController mapController = mapView.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(eventLocation);

        Marker marker = new Marker(mapView);
        marker.setPosition(eventLocation);
        marker.setTitle("Event location!");
        mapView.getOverlays().add(marker);

        mapView.invalidate();
    }

    private void removeAllFromBackStack(){
        FragmentManager manager = requireActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStackImmediate(null, manager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void startNewChat(){
        CreateChatDTO chat = new CreateChatDTO(foundEvent.getEventOrganizer().getId(), currentUser.getId());
        Call<CreatedChatDTO> call = ClientUtils.chatService.createChat(chat);
        call.enqueue(new Callback<CreatedChatDTO>() {

            @Override
            public void onResponse(Call<CreatedChatDTO> call, Response<CreatedChatDTO> response) {
                if (response.isSuccessful()) {
                    CreatedChatDTO createdChat = response.body();
                    removeAllFromBackStack();
                    FragmentTransition.to(ChatTabFragment.newInstance(currentUser), getActivity(), false, R.id.mainScreenFragment);
                    FragmentTransition.to(SingleChatFragment.newInstance(currentUser, createdChat.getEventOrganizer(), true), getActivity(), true, R.id.mainScreenFragment);
                }
            }

            @Override
            public void onFailure(Call<CreatedChatDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }
}