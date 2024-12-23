package com.example.eventplanner.fragments.home_screen_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.NotificationAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.notification.GetNotificationDTO;
import com.example.eventplanner.model.AuthenticatedUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private GetAuthenticatedUserDTO user;
    private ArrayList<GetNotificationDTO> userNotifications;
    private NotificationAdapter notificationAdapter;
    private RecyclerView recyclerView;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    public static NotificationsFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            user = getArguments().getParcelable("currentUser");
        }
        userNotifications = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        setUpRecyclerView(view);
        loadNotifications();
        return view;
    }

    private void loadNotifications(){ // setUpRecyclerView must be called before this method
        Call<ArrayList<GetNotificationDTO>> call = ClientUtils.notificationService.findByReceiverId(user.getId());
        call.enqueue(new Callback<ArrayList<GetNotificationDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<GetNotificationDTO>> call, Response<ArrayList<GetNotificationDTO>> response) {
                if (response.isSuccessful()) {
                    userNotifications = response.body();
                    notificationAdapter = new NotificationAdapter(userNotifications, getContext());
                    recyclerView.setAdapter(notificationAdapter);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetNotificationDTO>> call, Throwable t) {
                Log.i("Notifications", t.getMessage());
            }
        });
    }

    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.notificationRecyclerView);
        LinearLayoutManager layoutManagerServiceProduct = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerServiceProduct);
    }
}