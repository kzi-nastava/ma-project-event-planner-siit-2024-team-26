package com.example.eventplanner.dto.address;

import com.example.eventplanner.dto.location.CreateLocationDTO;
import com.example.eventplanner.dto.location.GetLocationDTO;

public class CreateAddressDTO {
    private String country;
    private String city;
    private String street;
    private int number;
    private CreateLocationDTO location;

    public CreateAddressDTO() {
    }

    public CreateAddressDTO(String country, String city, String street, int number, CreateLocationDTO location) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.number = number;
        this.location = location;
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

    public CreateLocationDTO getLocation() {
        return location;
    }

    public void setLocation(CreateLocationDTO location) {
        this.location = location;
    }
}
