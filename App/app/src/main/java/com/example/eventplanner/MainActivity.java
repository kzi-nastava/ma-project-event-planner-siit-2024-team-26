package com.example.eventplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eventplanner.clients.ClientUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splashscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ClientUtils.setContext(getApplicationContext());

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            Uri data = intent.getData();

            // Preuzmi query parametar "email"
            String email = data.getQueryParameter("email");
            if (email != null) {
                Bundle args = new Bundle();
                args.putString("givenEmail", email);
                Intent newIntent = new Intent(MainActivity.this, RegisterActivity.class);
                newIntent.putExtras(args);
                startActivity(newIntent);
                finish();
            }
        }else{
            doTransition();
        }
    }

    private void doTransition() {
        Timer transitionTimer = new Timer();
        TimerTask transitionTask = new TimerTask() {
            @Override
            public void run() {
                if (ClientUtils.getTokenManager().getToken() != null){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        };
        transitionTimer.schedule(transitionTask, 1000);
    }
}