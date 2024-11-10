package com.example.eventplanner.model;

public class ServiceProduct {
    private String name;
    private double price;
    private double discount;
    private Boolean isAvailable;

    private double grade;
    private String category;
    private int image;

    public ServiceProduct(String name, double price, double discount, Boolean isAvailable, double grade, String category, int image) {
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.isAvailable = isAvailable;
        this.grade = grade;
        this.category = category;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
