package com.example.eventplanner.model;

public class EventOrganizer extends AuthenticatedUser{
    public EventOrganizer(String email, String password, Boolean isActive, String role, String firstName, String lastName, String phoneNumber, Address address) {
        super(email, password, isActive, role, firstName, lastName, phoneNumber, address);
    }
}
