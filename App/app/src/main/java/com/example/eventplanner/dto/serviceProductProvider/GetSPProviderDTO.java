package com.example.eventplanner.dto.serviceProductProvider;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.dto.company.GetCompanyDTO;
import com.example.eventplanner.model.Role;

public class GetSPProviderDTO {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String image;
    private GetAddressDTO address;
    private Role role;
    private boolean isActive;
    private GetCompanyDTO company;

    public GetSPProviderDTO() {
    }

    public GetSPProviderDTO(Integer id, String email, String firstName, String lastName, String phoneNumber, String image, GetAddressDTO address, Role role, boolean isActive, GetCompanyDTO company) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.address = address;
        this.role = role;
        this.isActive = isActive;
        this.company = company;
    }

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

    public GetAddressDTO getAddress() {
        return address;
    }

    public void setAddress(GetAddressDTO address) {
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

    public GetCompanyDTO getCompany() {
        return company;
    }

    public void setCompany(GetCompanyDTO company) {
        this.company = company;
    }
}
