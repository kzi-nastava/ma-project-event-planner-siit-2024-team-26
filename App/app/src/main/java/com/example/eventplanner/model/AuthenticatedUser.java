package com.example.eventplanner.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.event.GetEventDTO;

import java.util.ArrayList;
import java.util.List;

public class AuthenticatedUser implements Parcelable {

    private Integer id;
    private String email;
    private String password;
    private Boolean isActive;
    private String role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;
    private List<Event> favouriteEvents;
    private List<Event> goingToEvents;

    public AuthenticatedUser(Integer id, String email, String password, Boolean isActive, String role,
                             String firstName, String lastName, String phoneNumber, Address address,
                             List<Event> favouriteEvents, List<Event> goingToEvents) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.favouriteEvents = favouriteEvents;
        this.goingToEvents = goingToEvents;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Event> getFavouriteEvents() {
        return favouriteEvents;
    }

    public void setFavouriteEvents(List<Event> favouriteEvents) {
        this.favouriteEvents = favouriteEvents;
    }

    public List<Event> getGoingToEvents() {
        return goingToEvents;
    }

    public void setGoingToEvents(List<Event> goingToEvents) {
        this.goingToEvents = goingToEvents;
    }

    protected AuthenticatedUser(Parcel in) {
        id = in.readInt();
        email = in.readString();
        password = in.readString();
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        role = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
        /*favouriteEvents = new ArrayList<>();
        in.readTypedList(favouriteEvents, Event.CREATOR);
        goingToEvents = new ArrayList<>();
        in.readTypedList(goingToEvents, Event.CREATOR);*/
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeByte((byte) (isActive == null ? 0 : isActive ? 1 : 2));
        dest.writeString(role);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeParcelable(address, flags);
        // dest.writeTypedList(favouriteEvents);
        // dest.writeTypedList(goingToEvents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AuthenticatedUser> CREATOR = new Creator<AuthenticatedUser>() {
        @Override
        public AuthenticatedUser createFromParcel(Parcel in) {
            return new AuthenticatedUser(in);
        }

        @Override
        public AuthenticatedUser[] newArray(int size) {
            return new AuthenticatedUser[size];
        }
    };
}
