package com.example.eventplanner.model;

import android.os.Parcel;

public class EventOrganizer extends AuthenticatedUser{
    public EventOrganizer(Integer id, String email, String password, Boolean isActive, String role, String firstName, String lastName, String phoneNumber, Address address) {
        super(id, email, password, isActive, role, firstName, lastName, phoneNumber, address);
    }

    protected EventOrganizer(Parcel in) {
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

    public static final Creator<EventOrganizer> CREATOR = new Creator<EventOrganizer>() {
        @Override
        public EventOrganizer createFromParcel(Parcel in) {
            return new EventOrganizer(in);
        }

        @Override
        public EventOrganizer[] newArray(int size) {
            return new EventOrganizer[size];
        }
    };
}
