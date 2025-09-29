package com.example.eventplanner;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.authorization.TokenManager;
import com.example.eventplanner.databinding.ActivityUserDetailsBinding;
import com.example.eventplanner.dto.authenticatedUser.GetAuthenticatedUserDTO;
import com.example.eventplanner.fragments.details.UserLoggedDetailsFragment;
import com.example.eventplanner.model.Role;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityUserDetailsBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private GetAuthenticatedUserDTO user;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarAlt);

        drawerLayout = binding.drawerLayoutAlt;
        toggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbarAlt,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navViewAlt.setNavigationItemSelectedListener(this);

        tokenManager = new TokenManager(this); // <- dodato

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_alt, new UserLoggedDetailsFragment())
                    .commit();
        }

        String email = tokenManager.getEmail(tokenManager.getToken());
        if (email == null) {
            Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            return;
        }

        ClientUtils.authenticatedUserService.getUserByEmail(email).enqueue(new Callback<GetAuthenticatedUserDTO>() {
            @Override
            public void onResponse(@NonNull Call<GetAuthenticatedUserDTO> call, @NonNull Response<GetAuthenticatedUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body(); // <- sada postavljamo globalnu promenljivu
                    setNavigationDrawerMenu();

                } else {
                    Toast.makeText(UserDetailsActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GetAuthenticatedUserDTO> call, @NonNull Throwable t) {
                Toast.makeText(UserDetailsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.homePageButton) {
            // Zatvori trenutnu aktivnost i vrati se na HomeActivity
            finish();
        }
        // dodaj druge opcije ako treba

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setNavigationDrawerMenu(){
        NavigationView navigationView = findViewById(R.id.nav_view_alt);
        View headerView = navigationView.getHeaderView(0);
        TextView drawerNameTextView = headerView.findViewById(R.id.drawerName); // R.id.drawerName je ID vašeg TextView-a u nav_header.xml
        drawerNameTextView.setText(user.getFirstName() + " " + user.getLastName());

        Menu navigationMenu = navigationView.getMenu();
        if (user.getRole() == Role.ADMINISTRATOR){
            navigationMenu.findItem(R.id.usersReportsButton).setVisible(true);
            navigationMenu.findItem(R.id.usersCommentsButton).setVisible(true);
        }

        String imageUrl = user.getImage(); // primer: https://mojserver.com/slike/user123.jpg
        ImageView piView = headerView.findViewById(R.id.imageView);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_person_24) // fallback ako slika još nije učitana
                    .error(R.drawable.baseline_person_24)           // fallback ako slika ne postoji
                    .into(piView);
        }
    }
}
