package com.example.eventplanner.dto.serviceProduct;

import com.example.eventplanner.model.ServiceProductType;

public class ServiceProductCardDTO {
    private Integer id;
    private String name;
    private String image;
    private double price;
    private ServiceProductType type;

    public ServiceProductCardDTO(){
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

    public ServiceProductType getType() {
        return type;
    }

    public void setType(ServiceProductType type) {
        this.type = type;
    }
}
