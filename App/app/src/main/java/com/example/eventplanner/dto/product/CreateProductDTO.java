package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;

public class CreateProductDTO {
    private String name;
    private GetCategoryDTO category;
    private String description;
    private double price;
    private double discount;
    private GetSPProviderDTO serviceProductProvider;
    private String newCategory;

    public CreateProductDTO() {
    }

    public CreateProductDTO(String name, GetCategoryDTO category, String description, double price, double discount, GetSPProviderDTO serviceProductProvider, String newCategory) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.serviceProductProvider = serviceProductProvider;
        this.newCategory = newCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public GetSPProviderDTO getServiceProductProvider() {
        return serviceProductProvider;
    }

    public void setServiceProductProvider(GetSPProviderDTO serviceProductProvider) {
        this.serviceProductProvider = serviceProductProvider;
    }

    public String getNewCategory() {
        return newCategory;
    }

    public void setNewCategory(String newCategory) {
        this.newCategory = newCategory;
    }
}
