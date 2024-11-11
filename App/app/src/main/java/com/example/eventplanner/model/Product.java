package com.example.eventplanner.model;

public class Product extends ServiceProduct {
    public Product(String name, int price, int discount, Boolean isAvailable, double grade, String category, int image) {
        super(name, price, discount, isAvailable, grade, category, image);
    }
}
