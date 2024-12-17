package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private String country;
    private String city;
    private String street;
    private int number;

    private Location location;

    public Address(String country, String city, String street, int number, Location location) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    protected Address(Parcel in) {
        country = in.readString();
        city = in.readString();
        street = in.readString();
        number = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(street);
        dest.writeInt(number);
        dest.writeParcelable(location, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

}
