package com.example.eventplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventOrganizer;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.ServiceProductProvider;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ArrayList<AuthenticatedUser> users;
    EditText emailTextView;
    EditText passwordTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        emailTextView = findViewById(R.id.email_input);
        Button registerButton = findViewById(R.id.register);
        users = new ArrayList<>();
        createUsers();
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                if (doesExists(email, password)){
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
        passwordTextView = findViewById(R.id.password_input);
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createUsers(){
        Address address = new Address("Serbia", "Belgrade", "Kalemegdanska", 13);
        Company company = new Company("mycompany@gmail.com", "BlueWhaleCompany", "We have the solution!", "1429319", address);
        AuthenticatedUser eventOrganizer = new EventOrganizer("eo@gmail.com", "sifrica", true,"eo", "Pera", "Peric", "12312412", address);
        AuthenticatedUser serviceProductProvider = new ServiceProductProvider("sp@gmail.com", "sifrica", true,"spp", "Milos", "Misic", "12312412", address, company);
        AuthenticatedUser administrator = new EventOrganizer("admin@gmail.com", "sifrica", true,"admin", "Dragan", "Dragic", "12312412", address);
        users.add(eventOrganizer);
        users.add(serviceProductProvider);
        users.add(administrator);
    }

    private boolean doesExists(String email, String password){
        for (AuthenticatedUser user: users){
            Log.i("MAME", user.getEmail());
            if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
}
