package com.jiat.travelraapp;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jiat.travelraapp.adapter.TourPackageAdapter;
import com.jiat.travelraapp.model.TourPackage;
import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TourPackageListActivity extends AppCompatActivity {
    private RecyclerView rvTours;
    private TourPackageAdapter adapter;
    private List<TourPackage> tourList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_package_list);

        // Existing initializations...
        db = FirebaseFirestore.getInstance();
        rvTours = findViewById(R.id.rvTourPackages);
        rvTours.setLayoutManager(new LinearLayoutManager(this));
        tourList = new ArrayList<>();
        adapter = new TourPackageAdapter(tourList);
        rvTours.setAdapter(adapter);

        // Call the search setup method
        setupSearch();

        loadToursFromFirebase();

        // Back navigation logic...
        findViewById(R.id.btnBackBoxList).setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();
        rvTours = findViewById(R.id.rvTourPackages);
        rvTours.setLayoutManager(new LinearLayoutManager(this));

        tourList = new ArrayList<>();
        adapter = new TourPackageAdapter(tourList);
        rvTours.setAdapter(adapter);

        loadToursFromFirebase();

        findViewById(R.id.toolbarTours).setOnClickListener(v -> finish());
    }

    private void loadToursFromFirebase() {
        db.collection("tour_packages").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                tourList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    TourPackage tour = doc.toObject(TourPackage.class);
                    // This line will no longer be red after Step 1
                    tour.setId(doc.getId());
                    tourList.add(tour);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        // Ensure this ID matches the EditText in activity_tour_package_list.xml
        EditText etSearch = findViewById(R.id.etSearchTours);

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
        List<TourPackage> filteredList = new ArrayList<>();

        for (TourPackage item : tourList) {
            // Filter by title (case-insensitive)
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the results
        adapter.filterList(filteredList);
    }
}
