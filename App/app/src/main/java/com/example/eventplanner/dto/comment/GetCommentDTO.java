package com.example.eventplanner.dto.comment;

import com.example.eventplanner.dto.authenticatedUser.AuthenticatedUserCommentDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCommentDTO;
import com.example.eventplanner.model.State;

public class GetCommentDTO {
    private Integer id;
    private String text;
    private AuthenticatedUserCommentDTO eventOrganizer;
    private ServiceProductCommentDTO serviceProduct;
    private State state;

    public GetCommentDTO(Integer id, String text, AuthenticatedUserCommentDTO eventOrganizer, ServiceProductCommentDTO serviceProduct, State state) {
        this.id = id;
        this.text = text;
        this.eventOrganizer = eventOrganizer;
        this.serviceProduct = serviceProduct;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public AuthenticatedUserCommentDTO getEventOrganizer() {
        return eventOrganizer;
    }

    public void setEventOrganizer(AuthenticatedUserCommentDTO eventOrganizer) {
        this.eventOrganizer = eventOrganizer;
    }

    public ServiceProductCommentDTO getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(ServiceProductCommentDTO serviceProduct) {
        this.serviceProduct = serviceProduct;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
