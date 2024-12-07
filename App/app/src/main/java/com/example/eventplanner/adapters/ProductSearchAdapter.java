package com.example.eventplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.dto.product.ProductCardDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;

import java.util.List;

public class ProductSearchAdapter extends RecyclerView.Adapter<ProductSearchAdapter.MyViewHolder> {

    private List<ProductCardDTO> foundProducts;
    private Context context;

    public ProductSearchAdapter(List<ProductCardDTO> products, Context context) {
        this.foundProducts = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductSearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card_search, parent, false);
        return new ProductSearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSearchAdapter.MyViewHolder holder, int position) {
        ProductCardDTO product = foundProducts.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()) + "€");
        Glide.with(this.context)
                .load(product.getImage()) // URL slike
                .into(holder.productImage);

    }


    @Override
    public int getItemCount() {
        return this.foundProducts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameSearch);
            productPrice = itemView.findViewById(R.id.productPriceSearch);
            productImage = itemView.findViewById(R.id.productImageSearch);

        }
    }
}
