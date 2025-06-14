package com.example.eventplanner.dto.authenticatedUser;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.address.GetAddressDTO;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.Role;

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
