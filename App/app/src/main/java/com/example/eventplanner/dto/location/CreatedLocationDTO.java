package com.example.eventplanner.dto.location;

public class CreatedLocationDTO {
    private Integer id;
    private double latitude;
    private double longitude;

    public CreatedLocationDTO() {
    }

    public CreatedLocationDTO(Integer id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
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
