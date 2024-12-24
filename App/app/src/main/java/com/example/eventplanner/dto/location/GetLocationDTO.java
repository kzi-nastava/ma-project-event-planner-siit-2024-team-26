package com.example.eventplanner.dto.location;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.eventplanner.model.Location;

public class GetLocationDTO implements Parcelable {

    private Integer id;
    private double latitude;
    private double longitude;

    public GetLocationDTO(){super();}

    public GetLocationDTO(Location location){
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    protected GetLocationDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GetLocationDTO> CREATOR = new Creator<GetLocationDTO>() {
        @Override
        public GetLocationDTO createFromParcel(Parcel in) {
            return new GetLocationDTO(in);
        }

        @Override
        public GetLocationDTO[] newArray(int size) {
            return new GetLocationDTO[size];
        }
    };
}
