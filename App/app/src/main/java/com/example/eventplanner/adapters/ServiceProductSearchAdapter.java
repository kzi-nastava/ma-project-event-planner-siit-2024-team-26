package com.example.eventplanner.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.ServiceDetailsFragment;
import com.example.eventplanner.model.ServiceProductType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceProductSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private GetAuthenticatedUserDTO currentUser;

    private List<ServiceProductCardDTO> serviceProducts;
    private Context context;

    private FragmentActivity fragmentActivity;

    private String reason;
    private GetChatDTO chat;

    public ServiceProductSearchAdapter(List<ServiceProductCardDTO> serviceProducts, Context context, FragmentActivity fragmentActivity, GetAuthenticatedUserDTO user) {
        this.serviceProducts = serviceProducts;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.currentUser = user;
    }

    @Override
    public int getItemViewType(int position) {
        // Vraća različite view type-ove na osnovu klase objekta
        ServiceProductCardDTO serviceProduct = this.serviceProducts.get(position);
        if (serviceProduct.getType() == ServiceProductType.PRODUCT) {
            return 1; // Tip za Product
        } else if (serviceProduct.getType() == ServiceProductType.SERVICE) {
            return 2; // Tip za Service
        }
        return 0; // Default tip
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_search, parent, false);
                return new ProductViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card_search, parent, false);
                return new ServiceViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProductCardDTO serviceProduct = this.serviceProducts.get(position);
        if (serviceProduct.getType() == ServiceProductType.PRODUCT){
            ProductViewHolder productHolder = (ProductViewHolder) holder;
            productHolder.productName.setText(serviceProduct.getName());
            productHolder.productPrice.setText(String.valueOf(serviceProduct.getPrice()) + "€");
            Glide.with(this.context)
                    .load(serviceProduct.getImage()) // URL slike
                    .into(productHolder.productImage);
        }
        else{
            ServiceViewHolder serviceHolder = (ServiceViewHolder) holder;
            serviceHolder.serviceName.setText(serviceProduct.getName());
            serviceHolder.servicePrice.setText(String.valueOf(serviceProduct.getPrice()) + "€");
            Glide.with(this.context)
                    .load(serviceProduct.getImage()) // URL slike
                    .into(serviceHolder.serviceImage);

            serviceHolder.serviceMoreInformationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkIsBlockedUser(holder, serviceProduct);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.serviceProducts.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameSearch);
            productPrice = itemView.findViewById(R.id.productPriceSearch);
            productImage = itemView.findViewById(R.id.productImageSearch);
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, servicePrice;
        ImageView serviceImage;

        Button serviceMoreInformationButton;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceMoreInformationButton = itemView.findViewById(R.id.moreInformationButton);
        }
    }

    private void checkIsBlockedUser(RecyclerView.ViewHolder holder, ServiceProductCardDTO service){
        if (this.currentUser != null){
            loadChat(holder, service);
        }else{
            FragmentTransition.to(ServiceDetailsFragment.newInstance(service.getId()), fragmentActivity, true, R.id.mainScreenFragment);
        }
    }

    private void loadChat(RecyclerView.ViewHolder holder, ServiceProductCardDTO service){

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
