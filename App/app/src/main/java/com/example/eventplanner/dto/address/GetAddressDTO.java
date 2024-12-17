package com.example.eventplanner.dto.address;

import com.example.eventplanner.dto.location.GetLocationDTO;
import com.example.eventplanner.model.Address;

public class GetAddressDTO {

    private Integer id;
    private String country;
    private String city;
    private String street;
    private int number;
    private GetLocationDTO location;

    public GetAddressDTO(){
        super();
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

    public GetLocationDTO getLocation() {
        return location;
    }

    public void setLocation(GetLocationDTO location) {
        this.location = location;
    }
}
