package com.jiat.travelraapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.jiat.travelraapp.model.TourPackage;

public class TourDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        TourPackage tour = (TourPackage) getIntent().getSerializableExtra("TOUR_DATA");

        if (tour != null) {
            // Main View Initializations
            TextView title = findViewById(R.id.tvCompleteTourTitle);
            TextView desc = findViewById(R.id.tvTourDescription);
            TextView price = findViewById(R.id.tvTourDetailPrice);
            ImageView image = findViewById(R.id.ivTourDetailImage);

            title.setText(tour.getTitle());
            desc.setText(tour.getDescription());
            price.setText(tour.getPrice());
            Glide.with(this).load(tour.getImageUrl()).into(image);

            setupInfoBoxes(tour);

            // Back Button Logic
            findViewById(R.id.btnBackBox).setOnClickListener(v -> finish());

            // FIX: BOOK NOW Logic
            MaterialButton btnBookNow = findViewById(R.id.btnBookTourNow);
            btnBookNow.setOnClickListener(v -> {
                // Ensure the context is explicitly the Activity class
                Intent intent = new Intent(TourDetailActivity.this, TaxiBookingActivity.class);
                startActivity(intent);
                // DO NOT call finish() here.
            });
        }
    }

    private void setupInfoBoxes(TourPackage tour) {
        View boxDuration = findViewById(R.id.boxDuration);
        ((TextView) boxDuration.findViewById(R.id.tvBoxText)).setText(tour.getDuration());
        ((ImageView) boxDuration.findViewById(R.id.ivBoxIcon)).setImageResource(R.drawable.schedule_24px);

        View boxPlaces = findViewById(R.id.boxPlaces);
        ((TextView) boxPlaces.findViewById(R.id.tvBoxText)).setText(tour.getPlaces());
        ((ImageView) boxPlaces.findViewById(R.id.ivBoxIcon)).setImageResource(R.drawable.location_on_24px);

        View boxMaxPeople = findViewById(R.id.boxMaxPeople);
        ((TextView) boxMaxPeople.findViewById(R.id.tvBoxText)).setText("Max 20");
        ((ImageView) boxMaxPeople.findViewById(R.id.ivBoxIcon)).setImageResource(R.drawable.groups_24px);
    }
}



