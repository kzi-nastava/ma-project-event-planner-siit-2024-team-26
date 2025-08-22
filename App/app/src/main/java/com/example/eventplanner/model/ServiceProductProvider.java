package com.example.eventplanner.model;

import android.os.Parcel;

import java.util.List;

public class ServiceProductProvider extends AuthenticatedUser {

    private Company company;
    public ServiceProductProvider(Integer id, String email, String password, Boolean isActive, String role, String firstName,
                                  String lastName, String phoneNumber, Address address, Company company, List<Event> favouriteEvents, List<Event> goingToEvents) {
        super(id, email, password, isActive, role, firstName, lastName, phoneNumber, address, favouriteEvents, goingToEvents);
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    protected ServiceProductProvider(Parcel in) {
        super(in); // Pozivamo konstruktor iz AuthenticatedUser da deserializuje atribute
        company = in.readParcelable(Company.class.getClassLoader()); // Deserializujemo objekat company
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // Pozivamo writeToParcel iz AuthenticatedUser
        dest.writeParcelable(company, flags); // Zapisujemo objekat company u Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceProductProvider> CREATOR = new Creator<ServiceProductProvider>() {
        @Override
        public ServiceProductProvider createFromParcel(Parcel in) {
            return new ServiceProductProvider(in);
        }

        @Override
        public ServiceProductProvider[] newArray(int size) {
            return new ServiceProductProvider[size];
        }
    };
}
