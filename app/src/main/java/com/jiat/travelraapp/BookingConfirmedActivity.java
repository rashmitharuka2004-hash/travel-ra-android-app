package com.jiat.travelraapp;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.StatusResponse;

public class BookingConfirmedActivity extends AppCompatActivity {

    private String bookingId;
    private double amount;
    private final static int PAYHERE_REQUEST = 1101;

    /**
     * PRODUCTION URL: Points to your Vercel deployment.
     * The "/verify/" suffix is mandatory to match your React Router path.
     */
    private final String VERIFY_TICKET_URL = "https://travel-ra-admin.vercel.app/verify/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmed);

        // Retrieve booking data from the previous Intent
        bookingId = getIntent().getStringExtra("booking_id");
        amount = getIntent().getDoubleExtra("total", 0.0);
        String pickup = getIntent().getStringExtra("pickup");
        String dropoff = getIntent().getStringExtra("dropoff");
        String vehicle = getIntent().getStringExtra("vehicle");

        // Initialize UI Components
        TextView tvId = findViewById(R.id.tvFinalBookingId);
        TextView tvPickup = findViewById(R.id.tvFinalPickup);
        TextView tvDropoff = findViewById(R.id.tvFinalDropoff);
        TextView tvVehicle = findViewById(R.id.tvFinalVehicle);
        ImageView ivQRCode = findViewById(R.id.ivBookingQRCode);

        // Bind data to the layout
        tvId.setText("#" + (bookingId != null ? bookingId : "N/A"));
        tvPickup.setText("Pickup: " + (pickup != null ? pickup : "N/A"));
        tvDropoff.setText("Drop-off: " + (dropoff != null ? dropoff : "N/A"));
        tvVehicle.setText("Vehicle: " + (vehicle != null ? vehicle : "N/A"));

        // GENERATE DYNAMIC QR CODE
        if (bookingId != null) {
            // Generates a URL like: https://travel-ra-admin.vercel.app/verify/TR123456
            String fullUrl = VERIFY_TICKET_URL + bookingId.trim();
            generateQRCode(fullUrl, ivQRCode);
        }

        // Initialize PayHere Payment
        findViewById(R.id.btnGoToPayment).setOnClickListener(v -> initPaymentRequest());
    }

    /**
     * Uses ZXing library to encode the Verification URL into a Bitmap
     */
    private void generateQRCode(String text, ImageView imageView) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 450, 450);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "QR Generation Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Configures the PayHere SDK for the transaction
     */
    private void initPaymentRequest() {
        InitRequest req = new InitRequest();
        req.setMerchantId("1224067"); // Replace with your Production Merchant ID if needed
        req.setCurrency("LKR");
        req.setAmount(amount);
        req.setOrderId(bookingId);
        req.setItemsDescription("TravelRa Taxi Booking: " + bookingId);
        req.setMerchantSecret("MTk1NzYwMjM0ODM5NzE5MDQ4MTcyOTU4MzQyODYzMTk0NDkxNzA0MQ==");

        // Customer details (Should ideally be fetched from Firebase Auth)
        req.getCustomer().setFirstName("Tharuka");
        req.getCustomer().setLastName("Jayaweera");
        req.getCustomer().setEmail("tharuka@example.com");
        req.getCustomer().setPhone("+94771234567");
        req.getCustomer().getAddress().setAddress("No. 1, Colombo");
        req.getCustomer().getAddress().setCity("Colombo");
        req.getCustomer().getAddress().setCountry("Sri Lanka");

        Intent intent = new Intent(this, PHMainActivity.class);
        intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
        PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL); // Toggle to LIVE_URL for production
        startActivityForResult(intent, PAYHERE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYHERE_REQUEST && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Payment Failed or Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
