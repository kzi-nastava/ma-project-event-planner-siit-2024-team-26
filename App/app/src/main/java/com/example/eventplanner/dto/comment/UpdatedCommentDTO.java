package com.example.eventplanner.dto.comment;

import com.example.eventplanner.dto.authenticatedUser.AuthenticatedUserCommentDTO;
import com.example.eventplanner.dto.serviceProduct.ServiceProductCommentDTO;
import com.example.eventplanner.model.State;

public class UpdatedCommentDTO {

    private Integer id;
    private String text;
    private AuthenticatedUserCommentDTO user;
    private State state;
    private ServiceProductCommentDTO serviceProduct;

    public UpdatedCommentDTO(Integer id, String text, AuthenticatedUserCommentDTO user, State state, ServiceProductCommentDTO serviceProduct) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.state = state;
        this.serviceProduct = serviceProduct;
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

    public AuthenticatedUserCommentDTO getUser() {
        return user;
    }

    public void setUser(AuthenticatedUserCommentDTO user) {
        this.user = user;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ServiceProductCommentDTO getServiceProduct() {
        return serviceProduct;
    }

    public void setServiceProduct(ServiceProductCommentDTO serviceProduct) {
        this.serviceProduct = serviceProduct;
    }
}
