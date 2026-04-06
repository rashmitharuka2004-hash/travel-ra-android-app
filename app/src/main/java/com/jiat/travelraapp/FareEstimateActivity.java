package com.jiat.travelraapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jiat.travelraapp.model.Vehicle;
import java.util.HashMap;
import java.util.Map;

public class FareEstimateActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private double totalFare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_estimate);

        db = FirebaseFirestore.getInstance();

        double distance = getIntent().getDoubleExtra("distance", 0.0);
        String pickup = getIntent().getStringExtra("pickup");
        String dropoff = getIntent().getStringExtra("dropoff");
        Vehicle selectedVehicle = (Vehicle) getIntent().getSerializableExtra("selected_vehicle");

        // FIX: If this is null, it stays on "Loading"
        if (selectedVehicle != null) {
            double baseFare = selectedVehicle.getBaseFare();
            double distCharge = distance * selectedVehicle.getPricePerKm();
            totalFare = baseFare + distCharge;

            TextView tvPickup = findViewById(R.id.tvSummaryPickup);
            TextView tvDropoff = findViewById(R.id.tvSummaryDropoff);
            TextView tvDist = findViewById(R.id.tvSummaryDist);
            TextView tvVeh = findViewById(R.id.tvSummaryVeh);
            TextView tvBase = findViewById(R.id.tvBaseFare);
            TextView tvDistChg = findViewById(R.id.tvDistCharge);
            TextView tvTotal = findViewById(R.id.tvTotalFare);
            ImageView ivVehicle = findViewById(R.id.ivSelectedVehicleFare);

            tvPickup.setText("Pickup: " + pickup);
            tvDropoff.setText("Drop-off: " + dropoff);
            tvDist.setText("Distance: " + String.format("%.1f", distance) + " km");
            tvVeh.setText("Vehicle: " + selectedVehicle.getName());
            tvBase.setText("$" + String.format("%.2f", baseFare));
            tvDistChg.setText("$" + String.format("%.2f", distCharge));
            tvTotal.setText("$" + String.format("%.2f", totalFare));

            if (selectedVehicle.getImageUrl() != null) {
                Glide.with(this).load(selectedVehicle.getImageUrl()).into(ivVehicle);
            }

            findViewById(R.id.btnConfirmBooking).setOnClickListener(v ->
                    confirmBooking(pickup, dropoff, selectedVehicle.getName()));
        } else {
            // Error handling to tell you why it's not loading
            Toast.makeText(this, "Error: Vehicle data missing!", Toast.LENGTH_LONG).show();
            finish();
        }

        findViewById(R.id.btnBackFare).setOnClickListener(v -> finish());
    }

    private void confirmBooking(String pickup, String dropoff, String vehicleName) {
        String bookingId = "TR" + (System.currentTimeMillis() / 1000);
        Map<String, Object> booking = new HashMap<>();
        booking.put("bookingId", bookingId);
        booking.put("pickup", pickup);
        booking.put("dropoff", dropoff);
        booking.put("vehicle", vehicleName);
        booking.put("amount", totalFare);
        booking.put("status", "Confirmed");

        db.collection("taxi_bookings").document(bookingId)
                .set(booking)
                .addOnSuccessListener(aVoid -> {
                    Intent intent = new Intent(this, BookingConfirmedActivity.class);
                    intent.putExtra("booking_id", bookingId);
                    intent.putExtra("total", totalFare);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show());
    }
}
