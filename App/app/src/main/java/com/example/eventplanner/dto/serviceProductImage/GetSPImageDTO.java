package com.example.eventplanner.dto.serviceProductImage;

public class GetSPImageDTO {
    private Integer id;
    private String imageSource;

    public GetSPImageDTO() {}

    public GetSPImageDTO(Integer id, String imageSource) {
        this.id = id;
        this.imageSource = imageSource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
