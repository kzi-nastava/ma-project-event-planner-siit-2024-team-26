package com.example.eventplanner;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.eventplanner.fragments.home_screen_fragments.EventTabFragment;
import com.example.eventplanner.fragments.FragmentTransition;
import com.example.eventplanner.fragments.home_screen_fragments.HomeScreenFragment;
import com.example.eventplanner.fragments.home_screen_fragments.ServiceProductTabFragment;
import com.example.eventplanner.fragments.home_screen_fragments.TopListsTabFragment;
import com.google.android.material.appbar.MaterialToolbar;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

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




}
