package com.example.eventplanner.dto.eventType;

import com.example.eventplanner.dto.category.GetCategoryDTO;

import java.util.List;

public class UpdateEventTypeDTO {
    private Integer id;
    private String description;
    private List<GetCategoryDTO> recommendedCategories;

    public UpdateEventTypeDTO() {
    }

    public UpdateEventTypeDTO(Integer id, String description, List<GetCategoryDTO> recommendedCategories) {
        this.id = id;
        this.description = description;
        this.recommendedCategories = recommendedCategories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
