package com.example.eventplanner.model;

public class ServiceProductImage {
    private Integer id;
    private String imageSource;
    private ServiceProduct serviceProduct;

    public ServiceProductImage() {}

    public ServiceProductImage(Integer id, String imageSource, ServiceProduct serviceProduct) {
        this.id = id;
        this.imageSource = imageSource;
        this.serviceProduct = serviceProduct;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public ServiceProduct getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(ServiceProduct serviceProduct) {
        this.serviceProduct = serviceProduct;
    }
}
