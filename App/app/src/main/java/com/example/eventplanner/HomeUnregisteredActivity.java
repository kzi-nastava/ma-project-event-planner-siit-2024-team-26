package com.example.eventplanner;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
import androidx.fragment.app.FragmentManager;

import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.home_screen_fragments.HomeScreenFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class HomeUnregisteredActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Boolean toExitApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_unregistered);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_unregistered_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        FragmentTransition.to(HomeScreenFragment.newInstance(), HomeUnregisteredActivity.this, false, R.id.mainScreenFragment);
        drawerLayout = findViewById(R.id.drawer_layout_unregistered);
        MaterialToolbar toolbar = findViewById(R.id.materialToolbar2);
        setSupportActionBar(toolbar);

        // Set up the ActionBarDrawerToggle with white color
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TextView loginMessage = findViewById(R.id.login_message);
        loginMessage.setText(getResources().getText(R.string.login_features));
        loginMessage.setTextColor(getResources().getColor(R.color.white, null));

        handleDrawerIconSelection(); //When icon on sidebar is pressed

        handleBackButtonClicked(); // When back button is pressed

        NavigationView navigationView = findViewById(R.id.navigation_drawer);

        // Set navigation item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Handle menu item clicks
                if (id == R.id.login_drawer_button) {
                    // Go to HomeActivity
                    startActivity(new Intent(HomeUnregisteredActivity.this, LoginActivity.class));
                    finish();
                } else if (id == R.id.register_drawer_button) {
                    // Go to ProfileActivity
                    startActivity(new Intent(HomeUnregisteredActivity.this, RegisterActivity.class));
                    finish();
                }

                // Close drawer after item selection
                drawerLayout.closeDrawers();

                return true;
            }
        });
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
    private void handleDrawerIconSelection(){
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getItemId() == R.id.login_drawer_button){
                    intent = new Intent(HomeUnregisteredActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.register_drawer_button){
                    intent = new Intent(HomeUnregisteredActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                finish();
                return false;
            }
        });
    }

    private void handleBackButtonClicked(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!isMoreFragment(fragmentManager)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeUnregisteredActivity.this);
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
                else{
                    fragmentManager.popBackStack();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private boolean isMoreFragment(FragmentManager fragmentManager){
        fragmentManager = getSupportFragmentManager();
        Log.i("NESTO", String.valueOf(fragmentManager.getBackStackEntryCount()));
        if (fragmentManager.getBackStackEntryCount() > 0) {
            return true;
        }
        return false;
    }

}
