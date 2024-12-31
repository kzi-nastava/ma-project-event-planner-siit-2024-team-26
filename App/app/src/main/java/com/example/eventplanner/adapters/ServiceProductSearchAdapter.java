package com.example.eventplanner.adapters;

import android.content.Context;
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
import com.example.eventplanner.dto.serviceProduct.ServiceProductCardDTO;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.details.ServiceDetailsFragment;
import com.example.eventplanner.model.ServiceProductType;

import java.util.List;

public class ServiceProductSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceProductCardDTO> serviceProducts;
    private Context context;

    private FragmentActivity fragmentActivity;

    public ServiceProductSearchAdapter(List<ServiceProductCardDTO> serviceProducts, Context context, FragmentActivity fragmentActivity) {
        this.serviceProducts = serviceProducts;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
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
                    FragmentTransition.to(ServiceDetailsFragment.newInstance(serviceProduct.getId()), fragmentActivity, true, R.id.mainScreenFragment);
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
}
