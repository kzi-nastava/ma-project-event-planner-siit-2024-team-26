package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {

    private String email;
    private String name;
    private String description;
    private String phoneNumber;
    private Address address;

    public Company(String email, String name, String description, String phoneNumber, Address address) {
        this.email = email;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    protected Company(Parcel in) {
        email = in.readString();
        name = in.readString();
        description = in.readString();
        phoneNumber = in.readString();
        address = in.readParcelable(Address.class.getClassLoader()); // Deserializujemo Address objekat
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(phoneNumber);
        dest.writeParcelable(address, flags); // Serijalizujemo Address objekat
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };
}
