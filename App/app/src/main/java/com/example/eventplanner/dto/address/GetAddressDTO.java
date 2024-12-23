package com.example.eventplanner.dto.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.dto.location.GetLocationDTO;
import com.example.eventplanner.model.Address;

public class GetAddressDTO implements Parcelable {

    private Integer id;
    private String country;
    private String city;
    private String street;
    private int number;
    private GetLocationDTO location;

    public GetAddressDTO(){
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public GetLocationDTO getLocation() {
        return location;
    }

    public void setLocation(GetLocationDTO location) {
        this.location = location;
    }

    protected GetAddressDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        country = in.readString();
        city = in.readString();
        street = in.readString();
        number = in.readInt();
        location = in.readParcelable(GetLocationDTO.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
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

    public static final Creator<GetAddressDTO> CREATOR = new Creator<GetAddressDTO>() {
        @Override
        public GetAddressDTO createFromParcel(Parcel in) {
            return new GetAddressDTO(in);
        }

        @Override
        public GetAddressDTO[] newArray(int size) {
            return new GetAddressDTO[size];
        }
    };
}
