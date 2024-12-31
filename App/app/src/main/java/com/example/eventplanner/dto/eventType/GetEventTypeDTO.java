package com.example.eventplanner.dto.eventType;

import com.example.eventplanner.dto.category.GetCategoryDTO;

import java.util.List;

public class GetEventTypeDTO {

    private Integer id;
    private String name;
    private String description;
    private boolean isActive;
    private List<GetCategoryDTO> recommendedCategories;

    public GetEventTypeDTO(){ super(); }

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<GetCategoryDTO> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(List<GetCategoryDTO> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }
}
