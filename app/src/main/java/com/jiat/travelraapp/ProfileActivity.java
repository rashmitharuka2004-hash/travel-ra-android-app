package com.jiat.travelraapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        loadUserData();
        setupSettingsRows();

        findViewById(R.id.btnContactUs).setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ContactInfoActivity.class));
        });

        findViewById(R.id.btnBackProfile).setOnClickListener(v -> finish());

        findViewById(R.id.btnProfileLogout).setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initViews() {
        // ID now exists in activity_profile.xml
        tvName = findViewById(R.id.tvProfileName);

        View emailLayout = findViewById(R.id.layoutEmail);
        View phoneLayout = findViewById(R.id.layoutPhone);

        tvEmail = emailLayout.findViewById(R.id.tvInfoValue);
        tvPhone = phoneLayout.findViewById(R.id.tvInfoValue);

        ((TextView) emailLayout.findViewById(R.id.tvInfoLabel)).setText("Email");
        ((ImageView) emailLayout.findViewById(R.id.ivInfoIcon)).setImageResource(R.drawable.mail_24px);

        ((TextView) phoneLayout.findViewById(R.id.tvInfoLabel)).setText("Phone");
        ((ImageView) phoneLayout.findViewById(R.id.ivInfoIcon)).setImageResource(R.drawable.ic_call_24px);
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            tvName.setText(documentSnapshot.getString("name"));
                            tvEmail.setText(documentSnapshot.getString("email"));
                            tvPhone.setText(documentSnapshot.getString("phone"));
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show());
        }
    }

    private void setupSettingsRows() {
        // FIXED: Targeted each unique include ID to prevent duplicates

        // 1. Payment Methods
        View paymentRow = findViewById(R.id.nav_payment_methods);
        ((TextView) paymentRow.findViewById(R.id.tvSettingTitle)).setText("Payment Methods");
        ((ImageView) paymentRow.findViewById(R.id.ivSettingIcon)).setImageResource(R.drawable.credit_card_24px);

        // 2. Notifications
        View notifyRow = findViewById(R.id.nav_notifications);
        ((TextView) notifyRow.findViewById(R.id.tvSettingTitle)).setText("Notifications");
        ((ImageView) notifyRow.findViewById(R.id.ivSettingIcon)).setImageResource(R.drawable.notifications_24px);

        // 3. App Settings
        View appSettingsRow = findViewById(R.id.nav_app_settings);
        ((TextView) appSettingsRow.findViewById(R.id.tvSettingTitle)).setText("App Settings");
        ((ImageView) appSettingsRow.findViewById(R.id.ivSettingIcon)).setImageResource(R.drawable.settings_24px);

        // 4. Help & Support
        View helpRow = findViewById(R.id.nav_help_support);
        ((TextView) helpRow.findViewById(R.id.tvSettingTitle)).setText("Help & Support");
        ((ImageView) helpRow.findViewById(R.id.ivSettingIcon)).setImageResource(R.drawable.help_24px);

        helpRow.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ContactInfoActivity.class));
        });
    }
}
