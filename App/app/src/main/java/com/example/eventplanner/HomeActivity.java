package com.example.eventplanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eventplanner.fragments.ServiceCreationFormFragment;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.home_screen_fragments.HomeScreenFragment;
import com.example.eventplanner.model.AuthenticatedUser;
import com.example.eventplanner.model.ServiceProductProvider;
import com.example.eventplanner.clients.service.EventService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {


    private EventService eventService;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    private AuthenticatedUser user;
    private Boolean isCreationFormShowed;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("User");
        }

        currentSelectedBottomIcon = R.id.home;

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

        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == currentSelectedBottomIcon){
                    return true;
                }
                if (item.getItemId() == R.id.create && user instanceof ServiceProductProvider) {
                    showCreateDialogSpp();
                    return true;
                }
                if (item.getItemId() == R.id.home){
                    FragmentTransition.to(HomeScreenFragment.newInstance(), HomeActivity.this, false, R.id.mainScreenFragment);
                    currentSelectedBottomIcon = R.id.home;
                    return true;
                }
                return false;
            }
        });


    }

    private void handleBackButtonClicked() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
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


}