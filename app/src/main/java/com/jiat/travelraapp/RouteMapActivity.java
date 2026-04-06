package com.jiat.travelraapp;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jiat.travelraapp.model.Vehicle;

public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String pickup, dropoff;
    private Vehicle selectedVehicle;
    private double simulatedDistance = 15.5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        // 1. Correctly retrieve the vehicle from TaxiBookingActivity
        pickup = getIntent().getStringExtra("pickup");
        dropoff = getIntent().getStringExtra("dropoff");
        selectedVehicle = (Vehicle) getIntent().getSerializableExtra("selected_vehicle");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.btnConfirmRoute).setOnClickListener(v -> {
            Intent intent = new Intent(this, FareEstimateActivity.class);
            intent.putExtra("pickup", pickup);
            intent.putExtra("dropoff", dropoff);
            intent.putExtra("distance", simulatedDistance);

            // 2. Forward the vehicle object to the next screen
            intent.putExtra("selected_vehicle", selectedVehicle);
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng srilanka = new LatLng(7.8731, 80.7718);
        mMap.addMarker(new MarkerOptions().position(srilanka).title("Trip Start"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(srilanka, 10));
    }
}
