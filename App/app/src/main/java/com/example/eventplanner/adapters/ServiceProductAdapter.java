package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceProduct;

import java.util.List;

public class ServiceProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ServiceProduct> items;

    public ServiceProductAdapter(List<ServiceProduct> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        // Vraća različite view type-ove na osnovu klase objekta
        ServiceProduct item = items.get(position);
        if (item instanceof Product) {
            return 1; // Tip za Product
        } else if (item instanceof Service) {
            return 2; // Tip za Service
        }
        return 0; // Default tip
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
                return new ProductViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_card, parent, false);
                return new ServiceViewHolder(view);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceProduct serviceProduct = items.get(position);

        if (holder instanceof ProductViewHolder) {
            Product product = (Product) serviceProduct;
            ProductViewHolder productHolder = (ProductViewHolder) holder;

            productHolder.productName.setText("Name: "+product.getName());
            productHolder.productCategory.setText("Category: "+product.getCategory());
            productHolder.productPrice.setText("Price: "+String.valueOf(product.getPrice())+"€");
            if (product.getAvailable()){
                productHolder.productIsAvailable.setText("Available");
            }else{
                productHolder.productIsAvailable.setText("Not available");
            }
            productHolder.productDiscount.setText("Discount: "+String.valueOf(product.getDiscount())+"%");
            productHolder.productGrade.setText("Grade: "+String.valueOf(product.getGrade()));
            productHolder.productImage.setImageResource(product.getImage());

        } else if (holder instanceof ServiceViewHolder) {
            Service service = (Service) serviceProduct;
            ServiceViewHolder serviceHolder = (ServiceViewHolder) holder;

            serviceHolder.serviceName.setText("Name: "+service.getName());
            serviceHolder.serviceCategory.setText("Category: "+service.getCategory());
            serviceHolder.servicePrice.setText("Price: "+String.valueOf(service.getPrice())+"€");
            if (service.getAvailable()){
                serviceHolder.serviceIsAvailable.setText("Available");
            }else{
                serviceHolder.serviceIsAvailable.setText("Not available");
            }
            serviceHolder.serviceDiscount.setText("Discount: "+String.valueOf(service.getDiscount())+"%");
            serviceHolder.serviceGrade.setText("Grade: "+String.valueOf(service.getGrade()));
            serviceHolder.serviceImage.setImageResource(service.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolders za Product i Service
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productCategory, productPrice, productDiscount,
                productIsAvailable, productGrade;
        ImageView productImage;

        public ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productCategory = itemView.findViewById(R.id.productCategory);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDiscount = itemView.findViewById(R.id.productDiscount);
            productIsAvailable = itemView.findViewById(R.id.isProductAvailable);
            productGrade = itemView.findViewById(R.id.productGrade);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView serviceName, serviceCategory, servicePrice, serviceDiscount,
                    serviceIsAvailable, serviceGrade;
        ImageView serviceImage;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceCategory = itemView.findViewById(R.id.serviceCategory);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            serviceDiscount = itemView.findViewById(R.id.serviceDiscount);
            serviceIsAvailable = itemView.findViewById(R.id.isServiceAvailable);
            serviceGrade = itemView.findViewById(R.id.serviceGrade);
            serviceImage = itemView.findViewById(R.id.serviceImage);
        }
    }
}
