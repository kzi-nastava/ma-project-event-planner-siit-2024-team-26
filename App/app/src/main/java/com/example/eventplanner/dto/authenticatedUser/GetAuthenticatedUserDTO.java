package com.example.eventplanner.dto.authenticatedUser;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.dto.event.GetEventDTO;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.Role;

import java.util.ArrayList;
import java.util.List;

public class GetAuthenticatedUserDTO implements Parcelable {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String image;
    private GetAddressDTO address;
    private Role role;
    private boolean isActive;
    private List<GetEventDTO> favouriteEvents;
    private List<GetEventDTO> goingToEvents;
    //private List<GetServiceProductDTO> favouriteServiceProducts;

    public GetAuthenticatedUserDTO() {super();}

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

    public List<GetEventDTO> getFavouriteEvents() {
        return favouriteEvents;
    }

    public void setFavouriteEvents(List<GetEventDTO> favouriteEvents) {
        this.favouriteEvents = favouriteEvents;
    }

    public List<GetEventDTO> getGoingToEvents() {
        return goingToEvents;
    }

    public void setGoingToEvents(List<GetEventDTO> goingToEvents) {
        this.goingToEvents = goingToEvents;
    }

    protected GetAuthenticatedUserDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        phoneNumber = in.readString();
        image = in.readString();
        address = in.readParcelable(GetAddressDTO.class.getClassLoader());
        role = Role.valueOf(in.readString());
        isActive = in.readByte() != 0;
        favouriteEvents = new ArrayList<>();
        in.readTypedList(favouriteEvents, GetEventDTO.CREATOR);
        goingToEvents = new ArrayList<>();
        in.readTypedList(goingToEvents, GetEventDTO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phoneNumber);
        dest.writeString(image);
        dest.writeParcelable(address, flags);
        dest.writeString(role.name());
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeTypedList(favouriteEvents);
        dest.writeTypedList(goingToEvents);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetAuthenticatedUserDTO> CREATOR = new Creator<GetAuthenticatedUserDTO>() {
        @Override
        public GetAuthenticatedUserDTO createFromParcel(Parcel in) {
            return new GetAuthenticatedUserDTO(in);
        }

        @Override
        public GetAuthenticatedUserDTO[] newArray(int size) {
            return new GetAuthenticatedUserDTO[size];
        }
    };
}
