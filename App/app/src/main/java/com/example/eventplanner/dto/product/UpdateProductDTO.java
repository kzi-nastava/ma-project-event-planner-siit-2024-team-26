package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.model.ServiceProductProvider;

public class UpdateProductDTO {
    private boolean isAvailable;
    private boolean isVisible;
    private GetCategoryDTO category;
    private String description;
    private double price;
    private double discount;
    private ServiceProductProvider serviceProductProvider;
    private String newCategory;

    public UpdateProductDTO() {
    }

    public UpdateProductDTO(boolean isAvailable, boolean isVisible, GetCategoryDTO category, String description, double price, double discount, ServiceProductProvider serviceProductProvider, String newCategory) {
        this.isAvailable = isAvailable;
        this.isVisible = isVisible;
        this.category = category;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.serviceProductProvider = serviceProductProvider;
        this.newCategory = newCategory;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public GetCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(GetCategoryDTO category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public ServiceProductProvider getServiceProductProvider() {
        return serviceProductProvider;
    }

    public void setServiceProductProvider(ServiceProductProvider serviceProductProvider) {
        this.serviceProductProvider = serviceProductProvider;
    }

    public String getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }
}
