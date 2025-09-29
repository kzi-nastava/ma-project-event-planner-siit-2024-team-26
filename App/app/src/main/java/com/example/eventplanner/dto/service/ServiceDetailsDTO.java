package com.example.eventplanner.dto.service;

import java.util.List;

public class ServiceDetailsDTO {
    private Integer id;
    private String name;
    private String categoryName;
    private double price;
    private double discount;
    private String description;
    private String specificity;
    private Boolean isAvailable;
    private List<String> images;

    public ServiceDetailsDTO(){}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecificity() {
        return specificity;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public List<String> getImages() {
        return images;
    }
}
