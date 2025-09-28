package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;
import com.example.eventplanner.model.ServiceProductType;
import com.google.gson.annotations.SerializedName;

public class CreatedProductDTO {
    private Integer id;
    private String name;
    private GetCategoryDTO category;
    private String description;
    private double price;
    private double discount;
    private ServiceProductType serviceProductType;
    private Integer gradeSum;
    private Integer gradeCount;
    @SerializedName("available")
    private boolean isAvailable;
    @SerializedName("visible")
    private boolean isVisible;
    @SerializedName("deleted")
    private boolean isDeleted;
    private GetSPProviderDTO serviceProductProvider;

    public CreatedProductDTO() {
    }

    public CreatedProductDTO(Integer id, String name, GetCategoryDTO category, String description, double price, double discount, ServiceProductType serviceProductType, Integer gradeSum, Integer gradeCount, boolean isAvailable, boolean isVisible, boolean isDeleted, GetSPProviderDTO serviceProductProvider) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.serviceProductType = serviceProductType;
        this.gradeSum = gradeSum;
        this.gradeCount = gradeCount;
        this.isAvailable = isAvailable;
        this.isVisible = isVisible;
        this.isDeleted = isDeleted;
        this.serviceProductProvider = serviceProductProvider;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public ServiceProductType getServiceProductType() {
        return serviceProductType;
    }

    public void setServiceProductType(ServiceProductType serviceProductType) {
        this.serviceProductType = serviceProductType;
    }

    public Integer getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(Integer gradeSum) {
        this.gradeSum = gradeSum;
    }

    public Integer getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(Integer gradeCount) {
        this.gradeCount = gradeCount;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public GetSPProviderDTO getServiceProductProvider() {
        return serviceProductProvider;
    }

    public void setServiceProductProvider(GetSPProviderDTO serviceProductProvider) {
        this.serviceProductProvider = serviceProductProvider;
    }
}
