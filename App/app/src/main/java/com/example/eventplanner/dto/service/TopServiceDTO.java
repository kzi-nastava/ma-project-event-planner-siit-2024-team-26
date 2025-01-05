package com.example.eventplanner.dto.service;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;

import java.util.List;

public class TopServiceDTO {
    private Integer id;
    private String name;
    private String description;
    private double price;
    private List<String> images;
    private ChatAuthenticatedUserDTO serviceProductProvider;

    public TopServiceDTO(){}

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public ChatAuthenticatedUserDTO getServiceProductProvider() {
        return serviceProductProvider;
    }

    public void setServiceProductProvider(ChatAuthenticatedUserDTO serviceProductProvider) {
        this.serviceProductProvider = serviceProductProvider;
    }
}
