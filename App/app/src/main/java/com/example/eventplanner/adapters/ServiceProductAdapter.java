package com.example.eventplanner.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceProduct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ServiceProductAdapter extends ArrayAdapter<ServiceProduct> {

    ArrayList<ServiceProduct> aServiceProducts;
    View productsView;
    public ServiceProductAdapter(@NonNull Context context, ArrayList<ServiceProduct> serviceProducts) {
        super(context, R.layout.product_card);
        this.aServiceProducts = serviceProducts;
    }

    @Override
    public int getCount() {
        return aServiceProducts.size();
    }

    @Nullable
    @Override
    public ServiceProduct getItem(int position) {
        return aServiceProducts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ServiceProduct serviceProduct = getItem(position);

        if (serviceProduct instanceof Product) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.product_card, parent, false);
        }
        else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_card, parent, false);

        }
        if(serviceProduct != null){
            if (serviceProduct instanceof Product){
                TextView productName = convertView.findViewById(R.id.productName);
                TextView productCategory = convertView.findViewById(R.id.productCategory);
                TextView productPrice = convertView.findViewById(R.id.productPrice);
                TextView productDiscount = convertView.findViewById(R.id.productDiscount);
                TextView productAvailable = convertView.findViewById(R.id.isProductAvailable);
                TextView productGrade = convertView.findViewById(R.id.productGrade);
                ImageView productImage = convertView.findViewById(R.id.productImage);

                productName.setText(serviceProduct.getName());
                productCategory.setText(serviceProduct.getCategory());
                productPrice.setText(String.valueOf(serviceProduct.getPrice()));
                productDiscount.setText(String.valueOf(serviceProduct.getDiscount()));
                productImage.setImageResource(serviceProduct.getImage());
                if(serviceProduct.getAvailable()){
                    productAvailable.setText("Available");
                }else{
                    productAvailable.setText("Not available");
                }
                productGrade.setText(String.valueOf(serviceProduct.getGrade()));
            }

            if (serviceProduct instanceof Service){
                TextView serviceName = convertView.findViewById(R.id.serviceName);
                TextView serviceCategory = convertView.findViewById(R.id.serviceCategory);
                TextView servicePrice = convertView.findViewById(R.id.servicePrice);
                TextView serviceDiscount = convertView.findViewById(R.id.serviceDiscount);
                TextView serviceAvailable = convertView.findViewById(R.id.isServiceAvailable);
                TextView serviceGrade = convertView.findViewById(R.id.serviceGrade);
                ImageView serviceImage = convertView.findViewById(R.id.serviceImage);

                serviceName.setText(serviceProduct.getName());
                serviceCategory.setText(serviceProduct.getCategory());
                servicePrice.setText(String.valueOf(serviceProduct.getPrice()));
                serviceDiscount.setText(String.valueOf(serviceProduct.getDiscount()));
                serviceImage.setImageResource(serviceProduct.getImage());
                if(serviceProduct.getAvailable()){
                    serviceAvailable.setText("Available");
                }else{
                    serviceAvailable.setText("Not available");
                }
                serviceGrade.setText(String.valueOf(serviceProduct.getGrade()));
            }
        }

        return convertView;
    }


}
