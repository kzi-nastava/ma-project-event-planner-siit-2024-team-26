package com.example.eventplanner.dto.product;

import com.example.eventplanner.dto.authenticatedUser.ChatAuthenticatedUserDTO;

public class ProductCardDTO {
    private Integer id;
    private String name;
    private String image;
    private double price;
    private ChatAuthenticatedUserDTO serviceProductProvider;

    public ProductCardDTO(){
        super();
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ChatAuthenticatedUserDTO getServiceProductProvider() {
        return serviceProductProvider;
    }

    public void setServiceProductProvider(ChatAuthenticatedUserDTO serviceProductProvider) {
        this.serviceProductProvider = serviceProductProvider;
    }
}
