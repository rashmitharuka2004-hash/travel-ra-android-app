package com.jiat.travelraapp;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jiat.travelraapp.adapter.DestinationAdapter;
import com.jiat.travelraapp.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvDestinations;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;
    private FirebaseFirestore db;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);

        setupToolbarAndDrawer();
        updateNavHeader();
        setupRecyclerView();
        loadDestinationsFromFirestore();
        setupBottomNavigation();
        setupQuickAccessCards();
        setupSearch(); // Correctly calling search setup

        ImageView ivNotification = findViewById(R.id.ivNotification);
        ivNotification.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
    }

    // Moved out of onCreate to fix syntax errors
    private void setupSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void filter(String text) {
        List<Destination> filteredList = new ArrayList<>();
        for (Destination item : destinationList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void setupQuickAccessCards() {
        MaterialCardView cardTours = findViewById(R.id.cardTours);
        MaterialCardView cardTaxi = findViewById(R.id.cardTaxi);

        cardTours.setOnClickListener(v -> startActivity(new Intent(this, TourPackageListActivity.class)));
        cardTaxi.setOnClickListener(v -> startActivity(new Intent(this, TaxiBookingActivity.class)));
    }

    private void setupRecyclerView() {
        rvDestinations = findViewById(R.id.rvDestinations);
        rvDestinations.setLayoutManager(new LinearLayoutManager(this));
        destinationList = new ArrayList<>();
        adapter = new DestinationAdapter(destinationList);
        rvDestinations.setAdapter(adapter);
    }

    private void loadDestinationsFromFirestore() {
        db.collection("popular_destinations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        destinationList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Destination dest = document.toObject(Destination.class);
                            destinationList.add(dest);
                        }
                        adapter.setDestinations(destinationList);
                    } else {
                        Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.nav_header_name);
        TextView tvNavEmail = headerView.findViewById(R.id.nav_header_email);

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    tvNavName.setText(documentSnapshot.getString("name"));
                    tvNavEmail.setText(documentSnapshot.getString("email"));
                }
            });
        }

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_view_profile) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void setupToolbarAndDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) return true;
            else if (id == R.id.nav_tours) {
                startActivity(new Intent(this, TourPackageListActivity.class));
                return true;
            } else if (id == R.id.nav_taxi) {
                startActivity(new Intent(this, TaxiBookingActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}
