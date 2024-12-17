package com.example.eventplanner.dto.authenticatedUser;

import com.example.eventplanner.dto.address.CreatedAddressDTO;
import com.example.eventplanner.model.Role;

public class CreatedAuthenticatedUserDTO {

    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String image;
    private CreatedAddressDTO address;
    private Role role;
    private boolean isActive;

    public CreatedAuthenticatedUserDTO(){super();}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CreatedAddressDTO getAddress() {
        return address;
    }

    public void setAddress(CreatedAddressDTO address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
