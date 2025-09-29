package com.example.eventplanner.clients.authorization;

public class TokenResponse {
    String token;


    public TokenResponse(){super();}
    public TokenResponse(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
