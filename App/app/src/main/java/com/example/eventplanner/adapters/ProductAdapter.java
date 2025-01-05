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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.chat.GetChatDTO;
import com.example.eventplanner.dto.product.TopProductDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.ProductDetailsFragment;
import com.example.eventplanner.fragments.details.ServiceDetailsFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    private GetAuthenticatedUserDTO currentUser;
    private List<TopProductDTO> topProducts;
    private Context context;
    private FragmentActivity fragmentActivity;
    private String reason;
    private GetChatDTO chat;

    public ProductAdapter(List<TopProductDTO> topProducts, Context context, FragmentActivity fragmentActivity, GetAuthenticatedUserDTO user) {
        this.topProducts = topProducts;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
        this.currentUser = user;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new ProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        TopProductDTO product = topProducts.get(position);
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.valueOf(product.getPrice()) + "€");
        Glide.with(this.context)
                .load(product.getImages().get(0)) // URL slike
                .into(holder.productImage);

        holder.moreInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsBlockedUser(holder, product);
            }
        });
    }


    @Override
    public int getItemCount() {
        return this.topProducts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        ImageView productImage;
        Button moreInformationButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            moreInformationButton = itemView.findViewById(R.id.moreInformationButton);
        }
    }

    //Checks if user can open selected product details tab
    private void checkIsBlockedUser(ProductAdapter.MyViewHolder holder, TopProductDTO product){
        if (this.currentUser != null){
            loadChat(holder, product);
        }else{
            FragmentTransition.to(ProductDetailsFragment.newInstance(product.getId()), fragmentActivity, true, R.id.mainScreenFragment);
        }
    }

    private void loadChat(ProductAdapter.MyViewHolder holder, TopProductDTO product){

        Call<GetChatDTO> call = ClientUtils.chatService.getChat(currentUser.getId(), product.getServiceProductProvider().getId());
        call.enqueue(new Callback<GetChatDTO>() {

            @Override
            public void onResponse(Call<GetChatDTO> call, Response<GetChatDTO> response) {
                if (response.isSuccessful()) {
                    chat = response.body();
                    if (currentUser.getId() == chat.getEventOrganizer().getId()){
                        if (chat.isUser_1_blocked()){
                            reason = "You can't see more information because service product provider " + product.getServiceProductProvider().getFirstName() +
                                    " " + product.getServiceProductProvider().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because service product provider " + product.getServiceProductProvider().getFirstName() +
                                    " " + product.getServiceProductProvider().getLastName() + " is blocked!";
                            showBlockedDialog();
                        }else{
                            FragmentTransition.to(ProductDetailsFragment.newInstance(product.getId()), fragmentActivity, true, R.id.mainScreenFragment);
                        }

                    }else{ // If user is Authenticated user in chat table
                        if (chat.isUser_2_blocked()){
                            reason = "You can't see more information because service product provider " + product.getServiceProductProvider().getFirstName() +
                                    " " + product.getServiceProductProvider().getLastName() + " has blocked you!";
                            showBlockedDialog();
                        } else if (chat.isUser_1_blocked()) {
                            reason = "You can't see more information because service product provider " + product.getServiceProductProvider().getFirstName() +
                                    " " + product.getServiceProductProvider().getLastName() + " is blocked!";
                            showBlockedDialog();
                        } else{
                            FragmentTransition.to(ProductDetailsFragment.newInstance(product.getId()), fragmentActivity, true, R.id.mainScreenFragment);
                        }
                    }
                }else{
                    if (response.code() == 404){
                        FragmentTransition.to(ProductDetailsFragment.newInstance(product.getId()), fragmentActivity, true, R.id.mainScreenFragment);
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
