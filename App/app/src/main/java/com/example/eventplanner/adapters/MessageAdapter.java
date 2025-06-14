package com.example.eventplanner.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.message.GetMessageDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.ServiceDetailsFragment;
import com.example.eventplanner.model.Role;
import com.example.eventplanner.model.ServiceProductType;
import com.example.eventplanner.utils.DateStringFormatter;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GetMessageDTO> messages;
    private Context context;


    private GetAuthenticatedUserDTO currentUser;

    private ChatAuthenticatedUserDTO otherUser;


    public MessageAdapter(List<GetMessageDTO> messages, Context context, GetAuthenticatedUserDTO currentUser, ChatAuthenticatedUserDTO otherUser) {
        this.messages = messages;
        this.context = context;
        this.currentUser = currentUser;
        this.otherUser = otherUser;
    }

    @Override
    public int getItemViewType(int position) {
        GetMessageDTO messageDTO = this.messages.get(position);
        if (currentUser.getRole() == Role.EVENT_ORGANIZER) {
            if (currentUser.getId() == messageDTO.getEventOrganizer().getId()){
                if (messageDTO.isFromUser1()){
                    return 1;
                }else{
                    return 2;
                }
            }else{
                if (messageDTO.isFromUser1()){
                    return 2;
                }else{
                    return 1;
                }
            }
        } else  {
            if (currentUser.getId() == messageDTO.getAuthenticatedUser().getId() && !messageDTO.isFromUser1()){
                return 1;
            }else{
                return 2;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logged_in_user_message_card, parent, false);
                return new MessageAdapter.LoggedInUserMessageViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_user_message_card, parent, false);
                return new MessageAdapter.OtherUserMessageViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetMessageDTO message = this.messages.get(position);
        int viewType = getItemViewType(position);

        switch (viewType) {
            case 1:
                // Rad sa ViewHolder za prijavljenog korisnika
                LoggedInUserMessageViewHolder loggedInHolder = (LoggedInUserMessageViewHolder) holder;
                loggedInHolder.userMessage.setText(message.getText()); // Primer rada sa View
                loggedInHolder.messageDate.setText(DateStringFormatter.format(message.getTimeStamp(), "HH:mm"));
                loggedInHolder.dateText.setVisibility(View.GONE);
                if (position == 0){
                    loggedInHolder.dateText.setVisibility(View.VISIBLE);
                    loggedInHolder.dateText.setText(DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy."));
                }else{
                    String previousMessageTimestamp = DateStringFormatter.format(this.messages.get(position - 1).getTimeStamp(), "dd.MM.yyyy.");
                    String currentMessageTimestamp = DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy.");
                    if (!previousMessageTimestamp.equals(currentMessageTimestamp)){
                        loggedInHolder.dateText.setVisibility(View.VISIBLE);
                        loggedInHolder.dateText.setText(DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy."));
                    }
                }
                break;
            case 2:
                // Rad sa ViewHolder za drugog korisnika
                OtherUserMessageViewHolder otherUserHolder = (OtherUserMessageViewHolder) holder;
                otherUserHolder.userMessage.setText(message.getText()); // Primer rada sa View
                otherUserHolder.messageDate.setText(DateStringFormatter.format(message.getTimeStamp(), "HH:mm"));
                Glide.with(this.context)
                    .load(otherUser.getImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.e("Glide", "Image load failed for URL: " + otherUser.getImage(), e);
                                otherUserHolder.userImage.setVisibility(View.GONE); // Sakrij ako failuje učitavanje
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false; // Glide će postaviti sliku sam
                            }
                        })
                    .into(otherUserHolder.userImage);
                otherUserHolder.dateText.setVisibility(View.GONE);
                if (position == 0){
                    otherUserHolder.dateText.setVisibility(View.VISIBLE);
                    otherUserHolder.dateText.setText(DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy."));
                }else{
                    String previousMessageTimestamp = DateStringFormatter.format(this.messages.get(position - 1).getTimeStamp(), "dd.MM.yyyy.");
                    String currentMessageTimestamp = DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy.");
                    if (!previousMessageTimestamp.equals(currentMessageTimestamp)){
                        otherUserHolder.dateText.setVisibility(View.VISIBLE);
                        otherUserHolder.dateText.setText(DateStringFormatter.format(message.getTimeStamp(), "dd.MM.yyyy."));
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown view type");

        }


    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public static class LoggedInUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessage, messageDate, dateText;

        public LoggedInUserMessageViewHolder(View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.userMessage);
            messageDate = itemView.findViewById(R.id.messageDate);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }

    public static class OtherUserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessage, messageDate, dateText;
        ImageView userImage;

        public OtherUserMessageViewHolder(View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.userMessage);
            userImage = itemView.findViewById(R.id.userImage);
            messageDate = itemView.findViewById(R.id.messageDate);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }

    public void addItem(GetMessageDTO item) {
        messages.add(item);
        notifyItemInserted(messages.size() - 1);  // Obaveštava adapter da je stavka dodata
    }

}
