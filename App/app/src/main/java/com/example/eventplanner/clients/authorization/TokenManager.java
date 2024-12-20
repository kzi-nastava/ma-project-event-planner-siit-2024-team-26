package com.example.eventplanner.clients.authorization;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

import android.util.Base64;


public class TokenManager {

    private SharedPreferences sharedPreferences;

    public TokenManager(Context givenContext){
        this.sharedPreferences  = givenContext.getSharedPreferences("AuthToken", Context.MODE_PRIVATE);
    }

    public String getToken(){
        return this.sharedPreferences.getString("AuthToken", null);
    }

    public void saveToken(String token){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("AuthToken", token);
        editor.apply();
    }

    public void removeToken(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("AuthToken");
        editor.apply();
    }

    public String getEmail(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null; // Nevažeći token
            }

            String payload = parts[1]; // Payload je u drugom delu
            String decodedPayload = new String(Base64.decode(payload, Base64.URL_SAFE));

            // Parsiraj decodedPayload kao JSON
            JSONObject json = new JSONObject(decodedPayload);
            return json.getString("sub"); // 'sub' je standardni ključ za subject u JWT tokenu
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
