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
import com.example.eventplanner.adapters.MessageAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.message.GetMessageDTO;
import com.example.eventplanner.model.id.ChatId;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleChatFragment extends Fragment {

    private GetAuthenticatedUserDTO currentUser;
    private RecyclerView recyclerView;

    private ChatAuthenticatedUserDTO otherUser;
    private ArrayList<GetMessageDTO> userMessages;
    private MessageAdapter messageAdapter;

    public SingleChatFragment() {
        // Required empty public constructor
    }

    public static SingleChatFragment newInstance(GetAuthenticatedUserDTO currentUser, ChatAuthenticatedUserDTO otherUser) {
        SingleChatFragment fragment = new SingleChatFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        args.putParcelable("otherUser", otherUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = getArguments().getParcelable("currentUser");
            otherUser = getArguments().getParcelable("otherUser");
        }
        userMessages = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_chat, container, false);
        setUpRecyclerView(view);
        loadMessages();
        return view;
    }

    private void loadMessages(){ // setUpRecyclerView must be called before this method
        Call<ArrayList<GetMessageDTO>> call = ClientUtils.messageService.getByUsers(currentUser.getId(), otherUser.getId());
        call.enqueue(new Callback<ArrayList<GetMessageDTO>>() {

            @Override
            public void onResponse(Call<ArrayList<GetMessageDTO>> call, Response<ArrayList<GetMessageDTO>> response) {
                if (response.isSuccessful()) {
                    userMessages = response.body();
                    messageAdapter = new MessageAdapter(userMessages, getActivity(), currentUser, otherUser);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GetMessageDTO>> call, Throwable t) {
                Log.i("Messages", t.getMessage());
            }
        });
    }
    private void setUpRecyclerView(View view){
        recyclerView = view.findViewById(R.id.messagesRecycleView);
        LinearLayoutManager layoutManagerMessages= new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerMessages);
    }
}