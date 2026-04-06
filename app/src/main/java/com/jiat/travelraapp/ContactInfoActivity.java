package com.jiat.travelraapp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        TextView tvPhone = findViewById(R.id.tvAdminPhone);

        tvPhone.setOnClickListener(v -> {
            String phoneNumber = "+94740637699";
            // Use ACTION_DIAL to safely open the dialer with the number pre-filled
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });
    }
}
