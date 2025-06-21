package com.example.eventplanner.dto.serviceProduct;

import com.example.eventplanner.model.ServiceProductType;

public class ServiceProductCommentDTO {

    private Integer id;
    private String name;
    private String description;
    private ServiceProductType serviceProductType;

    public ServiceProductCommentDTO(Integer id, String name, String description, ServiceProductType serviceProductType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.serviceProductType = serviceProductType;
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

    public ServiceProductType getServiceProductType() {
        return serviceProductType;
    }

    public void setServiceProductType(ServiceProductType serviceProductType) {
        this.serviceProductType = serviceProductType;
    }
}
