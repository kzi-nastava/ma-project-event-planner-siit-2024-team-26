package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.dto.product.TopProductDTO;
import com.example.eventplanner.dto.service.TopServiceDTO;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder>{

    private List<TopProductDTO> topProducts;

    public ProductAdapter(List<TopProductDTO> topProducts) {
        this.topProducts = topProducts;
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
    }


    @Override
    public int getItemCount() {
        return this.topProducts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productDescription;
        TextView productPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);

        }
    }
}
