package com.example.eventplanner.dto.eventType;

import com.example.eventplanner.dto.category.GetCategoryDTO;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdatedEventTypeDTO {
    private Integer id;
    private String name;
    private String description;
    private List<GetCategoryDTO> recommendedCategories;
    @SerializedName("active")
    private boolean isActive;

    public UpdatedEventTypeDTO() {
    }

    public UpdatedEventTypeDTO(Integer id, String name, String description, List<GetCategoryDTO> recommendedCategories, boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recommendedCategories = recommendedCategories;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GetCategoryDTO> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(List<GetCategoryDTO> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
