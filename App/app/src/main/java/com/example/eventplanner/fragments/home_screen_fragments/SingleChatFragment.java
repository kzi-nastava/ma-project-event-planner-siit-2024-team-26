package com.example.eventplanner.fragments.home_screen_fragments;

import static io.reactivex.internal.operators.flowable.FlowableReplay.observeOn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.ChatAdapter;
import com.example.eventplanner.adapters.MessageAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.message.CreateMessageDTO;
import com.example.eventplanner.dto.message.GetMessageDTO;
import com.example.eventplanner.model.id.ChatId;
import com.example.eventplanner.services.WebSocketService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
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
    private ImageButton sendButton;
    private boolean isAuthenticatedUser;
    private TextView chatWithTextView;

    private String message;

    public SingleChatFragment() {
        // Required empty public constructor
    }

    public static SingleChatFragment newInstance(GetAuthenticatedUserDTO currentUser, ChatAuthenticatedUserDTO otherUser, boolean isAuthenticatedUser) {
        SingleChatFragment fragment = new SingleChatFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentUser", currentUser);
        args.putParcelable("otherUser", otherUser);
        args.putBoolean("isAuthenticatedUser", isAuthenticatedUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = getArguments().getParcelable("currentUser");
            otherUser = getArguments().getParcelable("otherUser");
            isAuthenticatedUser = getArguments().getBoolean("isAuthenticatedUser");
        }
        userMessages = new ArrayList<>();

        connectToSignal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_chat, container, false);
        setUpAttributes(view);
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
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

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

    private void setUpAttributes(View view){
        EditText messageInputEditText = view.findViewById(R.id.messageInput);
        chatWithTextView = view.findViewById(R.id.userFirstAndLastName);
        chatWithTextView.setText(otherUser.getFirstName() + " " + otherUser.getLastName());

        sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = messageInputEditText.getText().toString();
                    if (message != null && !message.equals("")){
                        sendMessage();
                        messageInputEditText.setText("");
                    }
            }
        });
    }


    private void sendMessage(){
        ChatAuthenticatedUserDTO authenticatedUserDTO;
        ChatAuthenticatedUserDTO eventOganizerDTO;
        boolean isFromUser1 = false;
        if (isAuthenticatedUser){
            authenticatedUserDTO = new ChatAuthenticatedUserDTO(currentUser);
            eventOganizerDTO = otherUser;
            isFromUser1 = false;
        }
        else{
            authenticatedUserDTO = otherUser;
            eventOganizerDTO = new ChatAuthenticatedUserDTO(currentUser);
            isFromUser1 = true;
        }

        CreateMessageDTO messageToSend = new CreateMessageDTO(eventOganizerDTO, authenticatedUserDTO, message, isFromUser1);
        WebSocketService.sendMessage(messageToSend);
        messageAdapter.addItem(new GetMessageDTO(messageToSend));
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void receiveMessage(GetMessageDTO receivedMessage){
        messageAdapter.addItem(receivedMessage);
        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private void connectToSignal(){
        WebSocketService.messageSignal
                .observeOn(AndroidSchedulers.mainThread()).
                subscribe(receivedMessage ->{
                            receiveMessage(receivedMessage);
                        },
                        throwable -> {
                            // Obradi grešku
                            Log.e("RXJavaError", "Greška u BehaviorSubject: ", throwable);
                        });
    }
}