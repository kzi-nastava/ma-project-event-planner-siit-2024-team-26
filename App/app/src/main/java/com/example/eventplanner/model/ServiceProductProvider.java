package com.example.eventplanner.model;

public class ServiceProductProvider extends AuthenticatedUser {

    private Company company;
    public ServiceProductProvider(String email, String password, Boolean isActive, String role, String firstName,
                                  String lastName, String phoneNumber, Address address, Company company) {
        super(email, password, isActive, role, firstName, lastName, phoneNumber, address);
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
