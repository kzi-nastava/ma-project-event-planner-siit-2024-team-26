package com.example.eventplanner.dto.comment;

import com.example.eventplanner.model.State;

public class UpdateCommentDTO {

    private String text;
    private State state;

    public UpdateCommentDTO(String text, State state) {
        this.text = text;
        this.state = state;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
