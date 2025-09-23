package com.example.eventplanner.dto.address;

import com.example.eventplanner.dto.location.CreatedLocationDTO;

public class CreatedAddressDTO {
    private Integer id;
    private String country;
    private String city;
    private String street;
    private int number;
    private CreatedLocationDTO location;

    public CreatedAddressDTO() {
    }

    public CreatedAddressDTO(Integer id, String country, String city, String street, int number, CreatedLocationDTO location) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public CreatedLocationDTO getLocation() {
        return location;
    }

    public void setLocation(CreatedLocationDTO location) {
        this.location = location;
    }
}
