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
import com.example.eventplanner.adapters.ChatAdapter;
import com.example.eventplanner.adapters.NotificationAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.notification.GetNotificationDTO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatTabFragment extends Fragment {


    private GetAuthenticatedUserDTO user;
    private ArrayList<GetChatDTO> userChats;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    public ChatTabFragment() {
        // Required empty public constructor
    }


    public static ChatTabFragment newInstance(GetAuthenticatedUserDTO currentUser) {
        ChatTabFragment fragment = new ChatTabFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable("currentUser");
        }
        userChats = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_tab, container, false);
        setUpRecyclerView(view);
        loadChats();
        return view;
    }

    private void loadChats(){ // setUpRecyclerView must be called before this method
        Call<ArrayList<GetChatDTO>> call = ClientUtils.chatService.getByUserId(user.getId());
        call.enqueue(new Callback<ArrayList<GetChatDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<GetChatDTO>> call, Response<ArrayList<GetChatDTO>> response) {
                if (response.isSuccessful()) {
                    userChats = response.body();
                    chatAdapter = new ChatAdapter(userChats, getContext(), getActivity(), user);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetChatDTO>> call, Throwable t) {
                Log.i("Chats", t.getMessage());
            }
        });
    }

    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.chatRecyclerView);
        LinearLayoutManager layoutManagerChat= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerChat);
    }
}