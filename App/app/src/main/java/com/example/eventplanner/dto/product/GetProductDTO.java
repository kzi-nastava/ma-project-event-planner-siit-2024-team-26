package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.example.eventplanner.model.ServiceProductType;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class GetProductDTO {
    private Integer id;
    private String name;
    private GetCategoryDTO category;
    private String description;
    private double price;
    private double discount;
    private ArrayList<String> images;
    private ServiceProductType serviceProductType;
    private Integer gradeSum;
    private Integer gradeCount;
    private ArrayList<Comment> comments;
    @SerializedName("available")
    private boolean isAvailable;
    @SerializedName("visible")
    private boolean isVisible;
    @SerializedName("deleted")
    private boolean isDeleted;
    private GetAuthenticatedUserDTO spProvider;

    public GetProductDTO() {
    }

    public GetProductDTO(Integer id, String name, GetCategoryDTO category, String description, double price, double discount, ArrayList<String> images, ServiceProductType serviceProductType, Integer gradeSum, Integer gradeCount, ArrayList<Comment> comments, boolean isAvailable, boolean isVisible, boolean isDeleted, GetAuthenticatedUserDTO spProvider) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.images = images;
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
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

    public GetAuthenticatedUserDTO getSpProvider() {
        return spProvider;
    }

    public void setSpProvider(GetAuthenticatedUserDTO spProvider) {
        this.spProvider = spProvider;
    }
}
