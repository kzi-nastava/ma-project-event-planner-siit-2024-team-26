package com.example.eventplanner.model;

public class Product extends ServiceProduct {
    public Product(String name, double price, double discount, Boolean isAvailable, double grade, String category, int image) {
        super(name, price, discount, isAvailable, grade, category, image);
    }
}
