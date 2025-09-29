package com.example.eventplanner.dto.company;

import com.example.eventplanner.dto.address.GetAddressDTO;

public class GetCompanyDTO {
    private Integer id;
    private String email;
    private String name;
    private String description;
    private String phoneNumber;
    private int gradeSum;
    private int gradeCount;
    private GetAddressDTO address;

    public GetCompanyDTO() {
    }

    public GetCompanyDTO(Integer id, String email, String name, String description, String phoneNumber, int gradeSum, int gradeCount, GetAddressDTO address) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.gradeSum = gradeSum;
        this.gradeCount = gradeCount;
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getGradeSum() {
        return gradeSum;
    }

    public void setGradeSum(int gradeSum) {
        this.gradeSum = gradeSum;
    }

    public int getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(int gradeCount) {
        this.gradeCount = gradeCount;
    }

    public GetAddressDTO getAddress() {
        return address;
    }

    public void setAddress(GetAddressDTO address) {
        this.address = address;
    }
}
