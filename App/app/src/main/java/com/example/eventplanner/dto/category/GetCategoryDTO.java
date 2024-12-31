package com.example.eventplanner.dto.category;

import com.example.eventplanner.model.State;

public class GetCategoryDTO {

    private Integer id;
    private String name;
    private String description;
    private State state;
    private boolean isDeleted;

    public GetCategoryDTO(){super();}

    public GetCategoryDTO(Integer id, String name, String description, State state, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.isDeleted = isDeleted;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
