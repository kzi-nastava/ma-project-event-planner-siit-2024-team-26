package com.example.eventplanner.model;

import android.os.Parcel;

import java.util.List;

public class Administrator extends AuthenticatedUser{
    public Administrator(Integer id, String email, String password, Boolean isActive, String role, String firstName, String lastName, String phoneNumber, Address address, List<Event> favouriteEvents, List<Event> goingToEvents) {
        super(id, email, password, isActive, role, firstName, lastName, phoneNumber, address, favouriteEvents, goingToEvents);
    }

    protected Administrator(Parcel in) {
        super(in); // Pozivamo konstruktor iz AuthenticatedUser da deserializuje atribute
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // Pozivamo writeToParcel iz AuthenticatedUser
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Administrator> CREATOR = new Creator<Administrator>() {
        @Override
        public Administrator createFromParcel(Parcel in) {
            return new Administrator(in);
        }

        @Override
        public Administrator[] newArray(int size) {
            return new Administrator[size];
        }
    };
}
