package com.example.eventplanner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.eventplanner.adapters.ServiceSearchAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.authorization.TokenManager;
import com.example.eventplanner.fragments.EventCreationFormFragment;
import com.example.eventplanner.fragments.home_screen_fragments.NotificationsFragment;
import com.example.eventplanner.services.WebSocketService;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.dto.service.ServiceCardDTO;
import com.example.eventplanner.fragments.ServiceCreationFormFragment;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.home_screen_fragments.HomeScreenFragment;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.Page;
import com.example.eventplanner.model.Role;
import com.example.eventplanner.model.ServiceProductProvider;
import com.example.eventplanner.clients.service.EventService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    private EventService eventService;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private GetAuthenticatedUserDTO user;
    private Boolean isCreationFormShowed;

    private Boolean toExitApplication;

    BottomNavigationView bottomNavigationView;
    int currentSelectedBottomIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        FragmentTransition.to(HomeScreenFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
        drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar2);
        setSupportActionBar(toolbar);

        // Set up the ActionBarDrawerToggle with white color
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        handleDrawerIconSelection(); //When icon on sidebar is pressed

        handleBackButtonClicked(); // When back button is pressed

        setUser();

        currentSelectedBottomIcon = R.id.home;

        checkForNotifications();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_nav_menu, menu);

        // Set menu icons to white
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable icon = item.getIcon();
            if (icon != null) {
                icon.mutate(); // Mutate to ensure changes only affect this instance
                icon.setTint(getResources().getColor(R.color.white));
            }
        }

        // Set initial icon for day/night toggle based on the current mode
        MenuItem toggleItem = menu.findItem(R.id.topSettings);
        updateToggleIcon(toggleItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.topSettings) {
            toggleTheme(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme(MenuItem toggleItem) {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Update the toggle icon to reflect the new mode
        updateToggleIcon(toggleItem);
        recreate(); // Recreate activity to apply theme change
    }

    private void updateToggleIcon(MenuItem toggleItem) {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            toggleItem.setIcon(R.drawable.baseline_light_mode_24); // Day mode icon
        } else {
            toggleItem.setIcon(R.drawable.baseline_dark_mode_24); // Night mode icon
        }

        // Ensure the icon color is set to white
        Drawable icon = toggleItem.getIcon();
        if (icon != null) {
            icon.mutate(); // Mutate to apply color only to this instance
            icon.setTint(getResources().getColor(R.color.white)); // Set the icon color to white
        }
    }

    // When button in sidebar is pressed
    private void handleDrawerIconSelection() {
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.logoutButton) { // Log out button
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Log out")
                            .setMessage("Are you sure you want to log out?")
                            .setPositiveButton("YES", (dialog, which) -> {
                                ClientUtils.removeToken(); // Removes token from SharedPreferences
                                stopBackgroundService(); // Stops foreground service ( WEBSOCKET )
                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            })
                            .setNegativeButton("NO", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setCancelable(true)
                            .show();

                }
                return false;
            }
        });

        // HANDLING BOTTOM TOOLBAR
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == currentSelectedBottomIcon){
                    return true;
                }
                if (item.getItemId() == R.id.create && user.getRole() == Role.SERVICE_PRODUCT_PROVIDER) {
                    showCreateDialogSpp();
                    return true;
                }
                if (item.getItemId() == R.id.home){
                    FragmentTransition.to(HomeScreenFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
                    currentSelectedBottomIcon = R.id.home;
                    return true;
                }
                if (item.getItemId() == R.id.notifications){
                    FragmentTransition.to(NotificationsFragment.newInstance(user), HomeActivity.this, false, R.id.mainScreenFragment);
                    currentSelectedBottomIcon = R.id.notifications;
                    return true;
                }
                if (item.getItemId() == R.id.create && user.getRole() == Role.EVENT_ORGANIZER){
                    FragmentTransition.to(EventCreationFormFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
                    currentSelectedBottomIcon = R.id.create;
                    return true;
                }
                return false;
            }
        });


    }

    private void handleBackButtonClicked() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
               if (!isMoreFragment(fragmentManager)) {
                   AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                   builder.setTitle("Closing application")
                           .setMessage("Are you sure you want to close application?")
                           .setPositiveButton("YES", (dialog, which) -> {
                               finish();
                           })
                           .setNegativeButton("NO", (dialog, which) -> {
                               dialog.dismiss();
                           })
                           .setCancelable(true)
                           .show();
               }else{
                   fragmentManager.popBackStack();
               }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showCreateDialogSpp() {
        String[] optionsArray = {"Service", "Product"};
        int checkedItem = -1;
        final boolean[] selected = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Event types")
                .setSingleChoiceItems(optionsArray, checkedItem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            selected[0] = true;
                            dialog.dismiss();
                            FragmentTransition.to(ServiceCreationFormFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
                            currentSelectedBottomIcon = R.id.create;
                        }
                    }
                }).
                setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                       if (!selected[0]){
                           FragmentTransition.to(HomeScreenFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
                            bottomNavigationView.setSelectedItemId(currentSelectedBottomIcon);
                       }
                    }
                });


        builder.show();
    }


    private boolean isMoreFragment(FragmentManager fragmentManager){
        fragmentManager = getSupportFragmentManager();
        Log.i("NESTO", String.valueOf(fragmentManager.getBackStackEntryCount()));
        if (fragmentManager.getBackStackEntryCount() > 0) {
            return true;
        }
        return false;
    }

    private void setUser(){
        if (getApplicationContext() != null) {
            TokenManager tokenManager = ClientUtils.getTokenManager();
            String email = tokenManager.getEmail(tokenManager.getToken());

            Call<GetAuthenticatedUserDTO> call = ClientUtils.authenticatedUserService.getUserByEmail(email);
            call.enqueue(new Callback<GetAuthenticatedUserDTO>() {

                @Override
                public void onResponse(Call<GetAuthenticatedUserDTO> call, Response<GetAuthenticatedUserDTO> response) {
                    if (response.isSuccessful()) {
                        user = response.body();
                        setNameInDrawerMenu();
                        runBackgroundService();
                    }
                }

                @Override
                public void onFailure(Call<GetAuthenticatedUserDTO> call, Throwable t) {
                    Log.i("POZIV", t.getMessage());
                }
            });

        }
    }

    private void setNameInDrawerMenu(){
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        View headerView = navigationView.getHeaderView(0);
        TextView drawerNameTextView = headerView.findViewById(R.id.drawerName); // R.id.drawerName je ID vašeg TextView-a u nav_header.xml
        drawerNameTextView.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void runBackgroundService(){
        if (!WebSocketService.isServiceRunning()) {
            Log.i("WebSocket", "Service is not running, starting...");
            Intent serviceIntent = new Intent(HomeActivity.this, WebSocketService.class);
            Bundle args = new Bundle();
            args.putString("email", user.getEmail());
            serviceIntent.putExtras(args);
            startForegroundService(serviceIntent);
        }
        else{
            Log.i("WebSocket", "Service is already running!");
        }
    }

    private void stopBackgroundService(){
        Intent serviceIntent = new Intent(HomeActivity.this, WebSocketService.class);
        stopService(serviceIntent);  // Prekida servis
    }

    private void checkForNotifications(){
        // Request notification permission if not already granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 123);
        }
    }


}