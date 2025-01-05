package com.example.eventplanner.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.event.TopEventDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.EventDetailsFragment;
import com.example.eventplanner.fragments.details.ServiceDetailsFragment;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.utils.DateStringFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private GetAuthenticatedUserDTO currentUser;
    private List<TopServiceDTO> topServices;
    private Context context;
    private FragmentActivity fragmentActivity;
    private String reason;
    private GetChatDTO chat;

    public ServiceAdapter(List<TopServiceDTO> topServices, Context context, FragmentActivity fragmentActivity, GetAuthenticatedUserDTO user) {
        this.topServices = topServices;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.currentUser = user;
    }

    @NonNull
    @Override
    public ServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card, parent, false);
        return new ServiceAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.MyViewHolder holder, int position) {
        TopServiceDTO service = topServices.get(position);
        holder.serviceName.setText(service.getName());
        holder.serviceDescription.setText(service.getDescription());
        holder.servicePrice.setText(String.valueOf(service.getPrice()) + "€");
        Glide.with(this.context)
                .load(service.getImages().get(0)) // URL slike
                .into(holder.serviceImage);

        holder.moreInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsBlockedUser(holder, service);
            }
        });
    }


    @Override
    public int getItemCount() {
        return topServices.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName;
        TextView serviceDescription;
        TextView servicePrice;
        ImageView serviceImage;
        Button moreInformationButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            moreInformationButton = itemView.findViewById(R.id.moreInformationButton);

        }
    }

    //Checks if user can open selected service details tab
    private void checkIsBlockedUser(ServiceAdapter.MyViewHolder holder, TopServiceDTO service){
        if (this.currentUser != null){
            loadChat(holder, service);
        }else{
            FragmentTransition.to(ServiceDetailsFragment.newInstance(service.getId()), fragmentActivity, true, R.id.mainScreenFragment);
        }
    }

    private void loadChat(ServiceAdapter.MyViewHolder holder, TopServiceDTO service){

        Call<GetChatDTO> call = ClientUtils.chatService.getChat(currentUser.getId(), service.getServiceProductProvider().getId());
        call.enqueue(new Callback<GetChatDTO>() {

            @Override
            public void onResponse(Call<GetChatDTO> call, Response<GetChatDTO> response) {
                if (response.isSuccessful()) {
                    chat = response.body();
                    if (currentUser.getId() == chat.getEventOrganizer().getId()){
                        if (chat.isUser_1_blocked()){
                            reason = "You can't see more information because service product provider " + service.getServiceProductProvider().getFirstName() +
                                    " " + service.getServiceProductProvider().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because service product provider " + service.getServiceProductProvider().getFirstName() +
                                    " " + service.getServiceProductProvider().getLastName() + " is blocked!";
                            showBlockedDialog();
                        }else{
                            FragmentTransition.to(ServiceDetailsFragment.newInstance(service.getId()), fragmentActivity, true, R.id.mainScreenFragment);
                        }

                    }else{ // If user is Authenticated user in chat table
                        if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because service product provider " + service.getServiceProductProvider().getFirstName() +
                                    " " + service.getServiceProductProvider().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_1_blocked()) {
                            reason = "You can't see more information because service product provider " + service.getServiceProductProvider().getFirstName() +
                                    " " + service.getServiceProductProvider().getLastName() + " is blocked!";
                            showBlockedDialog();
                        } else{
                            FragmentTransition.to(ServiceDetailsFragment.newInstance(service.getId()), fragmentActivity, true, R.id.mainScreenFragment);
                        }
                    }
                }else{
                    if (response.code() == 404){
                        FragmentTransition.to(ServiceDetailsFragment.newInstance(service.getId()), fragmentActivity, true, R.id.mainScreenFragment);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetChatDTO> call, Throwable t) {
                Log.i("ChatEventAdapter", t.getMessage());
            }
        });
    }

    private void showBlockedDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);

        builder.setTitle("Access denied")
                .setMessage(reason);

        builder.setPositiveButton("I Understand", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
