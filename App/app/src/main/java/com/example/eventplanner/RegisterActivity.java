package com.example.eventplanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.QuickRegistrationForm;
import com.example.eventplanner.fragments.RoleFragment;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            FragmentTransition.to(QuickRegistrationForm.newInstance(bundle.getString("givenEmail")), RegisterActivity.this, false, R.id.containerRegister);
        }else{
            FragmentTransition.to(RoleFragment.newInstance(), RegisterActivity.this, false, R.id.containerRegister);
        }
    }


}