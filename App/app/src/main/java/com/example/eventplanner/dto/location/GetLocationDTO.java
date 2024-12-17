package com.example.eventplanner.dto.location;

import com.example.eventplanner.model.Location;

public class GetLocationDTO {

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
}
