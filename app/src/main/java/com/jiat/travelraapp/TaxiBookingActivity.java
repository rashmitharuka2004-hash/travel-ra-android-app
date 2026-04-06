package com.jiat.travelraapp;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jiat.travelraapp.adapter.VehicleAdapter;
import com.jiat.travelraapp.model.Vehicle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaxiBookingActivity extends AppCompatActivity implements VehicleAdapter.OnVehicleSelectedListener {

    private Button btnDate, btnTime, btnContinue;
    private EditText etPickup, etDropoff;
    private RecyclerView rvVehicleFleet;
    private VehicleAdapter vehicleAdapter;
    private List<Vehicle> vehicleList;
    private FirebaseFirestore db;
    private Vehicle selectedVehicle;

    // Variables to store selected data for comparison
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;
    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_booking);

        db = FirebaseFirestore.getInstance();

        try {
            if (!Places.isInitialized()) {
                String apiKey = "YOUR_ACTUAL_API_KEY_HERE";
                Places.initialize(getApplicationContext(), apiKey);
            }
        } catch (Exception e) {
            Log.e("PlacesError", "Initialization failed: " + e.getMessage());
        }

        initViews();
        setupVehicleRecyclerView();
        loadFleetFromFirebase();

        btnDate.setOnClickListener(v -> showDatePicker());
        btnTime.setOnClickListener(v -> showTimePicker());
        btnContinue.setOnClickListener(v -> handleNavigation());
        findViewById(R.id.btnBackTaxi).setOnClickListener(v -> finish());
    }

    private void handleNavigation() {
        if (etPickup.getText().toString().isEmpty() || etDropoff.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter locations", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isDateSelected || !isTimeSelected) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedVehicle == null) {
            Toast.makeText(this, "Please select a vehicle type", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, RouteMapActivity.class);
        intent.putExtra("pickup", etPickup.getText().toString());
        intent.putExtra("dropoff", etDropoff.getText().toString());
        intent.putExtra("date", btnDate.getText().toString());
        intent.putExtra("time", btnTime.getText().toString());
        intent.putExtra("selected_vehicle", selectedVehicle);
        startActivity(intent);
    }

    private void initViews() {
        btnDate = findViewById(R.id.btnSelectDate);
        btnTime = findViewById(R.id.btnSelectTime);
        btnContinue = findViewById(R.id.btnTaxiContinue);
        etPickup = findViewById(R.id.etPickup);
        etDropoff = findViewById(R.id.etDropoff);
        rvVehicleFleet = findViewById(R.id.rvVehicleFleet);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, day) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = day;
            isDateSelected = true;
            btnDate.setText(day + "/" + (month + 1) + "/" + year);

            // Reset time if date changes to today to force re-validation
            isTimeSelected = false;
            btnTime.setText("Select time");
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        // Validation: Block past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        if (!isDateSelected) {
            Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {

            // Validation: Check if selected time is in the past for TODAY
            if (selectedYear == now.get(Calendar.YEAR) &&
                    selectedMonth == now.get(Calendar.MONTH) &&
                    selectedDay == now.get(Calendar.DAY_OF_MONTH)) {

                if (hourOfDay < now.get(Calendar.HOUR_OF_DAY) ||
                        (hourOfDay == now.get(Calendar.HOUR_OF_DAY) && minute < now.get(Calendar.MINUTE))) {
                    Toast.makeText(this, "Cannot select a past time for today", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            selectedHour = hourOfDay;
            selectedMinute = minute;
            isTimeSelected = true;
            btnTime.setText(String.format("%02d:%02d", hourOfDay, minute));

        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);

        timePickerDialog.show();
    }

    // ... (rest of your existing methods: setupVehicleRecyclerView, loadFleetFromFirebase, onVehicleSelected)
    private void setupVehicleRecyclerView() {
        vehicleList = new ArrayList<>();
        vehicleAdapter = new VehicleAdapter(vehicleList, this);
        rvVehicleFleet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvVehicleFleet.setAdapter(vehicleAdapter);
    }

    private void loadFleetFromFirebase() {
        db.collection("vehicles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                vehicleList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Vehicle vehicle = doc.toObject(Vehicle.class);
                    vehicleList.add(vehicle);
                }
                vehicleAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onVehicleSelected(Vehicle vehicle) {
        this.selectedVehicle = vehicle;
    }
}
