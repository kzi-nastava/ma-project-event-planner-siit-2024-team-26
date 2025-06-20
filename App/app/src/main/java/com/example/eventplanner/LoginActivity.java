package com.example.eventplanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.adapters.EventSearchAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.authorization.TokenResponse;
import com.example.eventplanner.dto.authenticatedUser.LoginDTO;
import com.example.eventplanner.dto.event.EventCardDTO;
import com.example.eventplanner.dto.report.BannedDTO;
import com.example.eventplanner.model.Address;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.Company;
import com.example.eventplanner.model.EventOrganizer;
import com.example.eventplanner.model.Page;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.ServiceProductProvider;
import com.example.eventplanner.utils.DateStringFormatter;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String email = emailTextView.getText().toString();
                String password = passwordTextView.getText().toString();
                login();
//                AuthenticatedUser foundUser = doesExists(email, password);
//                if (foundUser != null){
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    Bundle args = new Bundle();
//                    args.putParcelable("User", foundUser);
//                    intent.putExtras(args);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });
        passwordTextView = findViewById(R.id.password_input);
        registerButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button unregisteredButton = findViewById(R.id.noaccount);

        // Set an OnClickListener to open the new activity
        unregisteredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the SecondActivity
                Intent intent = new Intent(LoginActivity.this, HomeUnregisteredActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void login(){
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        LoginDTO loginDTO = new LoginDTO(email, password);
        Call<Object> call = ClientUtils.authenticationService.login(loginDTO);
        call.enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    String responseBodyString = response.body().toString();
                    Log.i("login123", responseBodyString);
                    if (responseBodyString.contains("token=")) {
                        TokenResponse token = gson.fromJson(new Gson().toJson(response.body()), TokenResponse.class);
                        ClientUtils.saveToken(token);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (responseBodyString.contains("bannedUntil=")){
                        BannedDTO bannedDTO = gson.fromJson(new Gson().toJson(response.body()), BannedDTO.class);
                        String date = DateStringFormatter.format(bannedDTO.getBannedUntil().toString(), "dd.MM.yyyy. HH:mm");
                        String toastText = String.format("You are banned until %s !", date);
                        Toast.makeText(LoginActivity.this, toastText, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Invalid credentials!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("POZIV", t.getMessage());
                Toast.makeText(LoginActivity.this, "Somehing went wrong! Check connecting IP", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
