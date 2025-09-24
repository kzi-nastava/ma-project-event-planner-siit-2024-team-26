package com.example.eventplanner.fragments.details;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
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


import com.example.eventplanner.HomeActivity;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.IdentityHashMap;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
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
        if (foundEvent == null) {
            Toast.makeText(getContext(), "Event details not loaded yet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prikazujemo poruku da je preuzimanje počelo
        Snackbar.make(binding.getRoot(), "Starting download...", Snackbar.LENGTH_SHORT).show();

        // Pozivamo ispravljenu metodu servisa
        Call<ResponseBody> call = ClientUtils.eventService.exportEventToPdf(foundEvent.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Prebacujemo operaciju pisanja fajla na pozadinsku nit
                    // da ne bismo blokirali korisnički interfejs
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        boolean success = savePdfToDownloads(response.body());
                        // Vraćamo se na glavnu nit da prikažemo rezultat
                        getActivity().runOnUiThread(() -> {
                            if (success) {
                                Snackbar.make(binding.getRoot(), "PDF saved to Downloads folder.", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(binding.getRoot(), "Failed to save PDF.", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    });
                } else {
                    Snackbar.make(binding.getRoot(), "Download failed: " + response.message(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(binding.getRoot(), "Network Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean savePdfToDownloads(ResponseBody body) {
        String fileName = "Event_" + foundEvent.getId() + ".pdf";
        ContentResolver resolver = getContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        // Čuvamo fajl u javni "Downloads" folder
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues);

        if (uri != null) {
            try (InputStream inputStream = body.byteStream();
                 OutputStream outputStream = resolver.openOutputStream(uri)) {

                if (outputStream == null) return false;

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return true;

            } catch (IOException e) {
                Log.e("PDF_SAVE", "Error saving PDF", e);
                return false;
            }
        }
        return false;
    }

    private void onOrganizerClicked() {
        Integer organizerId = foundEvent.getEventOrganizer().getId();

        FragmentTransition.to(
                UserDetailsFragment.newInstance(organizerId),
                getActivity(),
                true,
                R.id.mainScreenFragment
        );
    }

    private void onChatClicked() {
        ChatAuthenticatedUserDTO otherUser = null;
        boolean isAuthenticatedUser = false;
        if (currentUser == null){
            Snackbar snackbar = Snackbar.make(binding.getRoot(), "No permission to chat with event organizer!", Snackbar.LENGTH_LONG);
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
            snackbar.show();
        }
        else if (chat != null){
            if (chat.getEventOrganizer().getId() == currentUser.getId()){
                otherUser = chat.getAuthenticatedUser();
                isAuthenticatedUser = false;
            }
            else{
                otherUser = chat.getEventOrganizer();
                isAuthenticatedUser = true;
            }

            HomeActivity homeActivity = (HomeActivity) getActivity();
            if (homeActivity != null) {
                homeActivity.setCurrentSelectedBottomIcon(R.id.chat);
                FragmentTransition.to(SingleChatFragment.newInstance(currentUser, otherUser, isAuthenticatedUser), getActivity(), true, R.id.mainScreenFragment);
            }

        }
        else if (chat == null){
            if (foundEvent.getEventOrganizer().getId() == currentUser.getId()){
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "You can't start chat with yourself!", Snackbar.LENGTH_LONG);
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
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


    private void startNewChat(){
        CreateChatDTO chat = new CreateChatDTO(foundEvent.getEventOrganizer().getId(), currentUser.getId());
        Call<CreatedChatDTO> call = ClientUtils.chatService.createChat(chat);
        call.enqueue(new Callback<CreatedChatDTO>() {

            @Override
            public void onResponse(Call<CreatedChatDTO> call, Response<CreatedChatDTO> response) {
                if (response.isSuccessful()) {
                    CreatedChatDTO createdChat = response.body();
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (homeActivity != null) {
                        homeActivity.setCurrentSelectedBottomIcon(R.id.chat);
                        FragmentTransition.to(SingleChatFragment.newInstance(currentUser, createdChat.getEventOrganizer(), true), getActivity(), true, R.id.mainScreenFragment);
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatedChatDTO> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
            }
        });
    }
}