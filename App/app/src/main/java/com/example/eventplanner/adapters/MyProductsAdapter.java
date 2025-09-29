package com.example.eventplanner.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.dto.product.GetProductDTO;

import java.util.List;

public class MyProductsAdapter extends RecyclerView.Adapter<MyProductsAdapter.ProductViewHolder> {

    private List<GetProductDTO> products;
    private final OnProductActionClickListener listener;

    public interface OnProductActionClickListener {
        void onInfoClicked(GetProductDTO product);
        void onEditClicked(GetProductDTO product);
        void onToggleVisibilityClicked(GetProductDTO product);
        void onDeleteClicked(GetProductDTO product);
    }

    public MyProductsAdapter(List<GetProductDTO> products, OnProductActionClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        GetProductDTO product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateProducts(List<GetProductDTO> newProducts) {
        this.products.clear();
        if (newProducts != null) {
            this.products.addAll(newProducts);
        }
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, category, price, availability;
        ImageButton btnInfo, btnEdit, btnToggle, btnDelete;
        ConstraintLayout cardLayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.product_title);
            description = itemView.findViewById(R.id.product_description);
            category = itemView.findViewById(R.id.product_category);
            price = itemView.findViewById(R.id.product_price);
            availability = itemView.findViewById(R.id.product_availability);
            btnInfo = itemView.findViewById(R.id.btn_info);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnToggle = itemView.findViewById(R.id.btn_toggle_visibility);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            cardLayout = itemView.findViewById(R.id.card_content_layout);
        }
        
        public void bind(final GetProductDTO product, final OnProductActionClickListener listener) {
            title.setText(product.getName());
            description.setText(product.getDescription());
            price.setText("Price: " + product.getPrice() + "€");

            if (product.getCategory() != null) {
                category.setText("Category: " + product.getCategory().getName());
            } else {
                category.setText("No category");
            }

            availability.setText("Available: " + (product.isAvailable() ? "Yes" : "No"));
            // availability.setTextColor(product.isAvailable() ? Color.parseColor("#8E8D8A") : Color.parseColor("#E85A4F"));

            // Stilovi na osnovu vidljivosti
            if (product.isVisible()) {
                cardLayout.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.accent))
                );
                btnToggle.setImageResource(R.drawable.baseline_toggle_on_24);
            } else {
                cardLayout.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.neutral))
                );
                btnToggle.setImageResource(R.drawable.baseline_toggle_off_24);
            }

            // Click listener-i
            btnInfo.setOnClickListener(v -> listener.onInfoClicked(product));
            btnEdit.setOnClickListener(v -> listener.onEditClicked(product));
            btnToggle.setOnClickListener(v -> listener.onToggleVisibilityClicked(product));
            btnDelete.setOnClickListener(v -> listener.onDeleteClicked(product));
        }
    }
}