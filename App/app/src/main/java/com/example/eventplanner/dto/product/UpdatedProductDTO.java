package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.dto.comment.UpdatedCommentDTO;
import com.example.eventplanner.dto.serviceProductProvider.GetSPProviderDTO;
import com.example.eventplanner.model.ServiceProductType;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdatedProductDTO {
    private Integer id;
    private String name;
    private GetCategoryDTO category;
    private String description;
    private double price;
    private double discount;
    private ServiceProductType serviceProductType;
    private Integer gradeSum;
    private Integer gradeCount;
    private ArrayList<UpdatedCommentDTO> comments;
    private GetSPProviderDTO spProvider;
    @SerializedName("available")
    private boolean isAvailable;
    @SerializedName("visible")
    private boolean isVisible;
    @SerializedName("deleted")
    private boolean isDeleted;

    public UpdatedProductDTO() {
    }

    public UpdatedProductDTO(Integer id, String name, GetCategoryDTO category, String description, double price, double discount, ServiceProductType serviceProductType, Integer gradeSum, Integer gradeCount, ArrayList<UpdatedCommentDTO> comments, GetSPProviderDTO spProvider, boolean isAvailable, boolean isVisible, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.serviceProductType = serviceProductType;
        this.gradeSum = gradeSum;
        this.gradeCount = gradeCount;
        this.comments = comments;
        this.isAvailable = isAvailable;
        this.isVisible = isVisible;
        this.isDeleted = isDeleted;
        this.spProvider = spProvider;
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

    public ArrayList<UpdatedCommentDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<UpdatedCommentDTO> comments) {
        this.comments = comments;
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

    public GetSPProviderDTO getSpProvider() {
        return spProvider;
    }

    public void setSpProvider(GetSPProviderDTO spProvider) {
        this.spProvider = spProvider;
    }
}
