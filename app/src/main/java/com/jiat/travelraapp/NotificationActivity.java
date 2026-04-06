package com.jiat.travelraapp;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.jiat.travelraapp.adapter.NotificationAdapter;
import com.jiat.travelraapp.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ensure you have created res/layout/activity_notification.xml
        setContentView(R.layout.activity_notification);

        db = FirebaseFirestore.getInstance();

        // 1. Initialize RecyclerView
        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        // 2. Initialize List and Adapter
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        rvNotifications.setAdapter(adapter);

        // 3. Back Button Navigation
        View btnBack = findViewById(R.id.btnBackNotif);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 4. Start Loading Data
        loadNotifications();
    }

    private void loadNotifications() {
        // Real-time listener to fetch notifications from Firestore
        db.collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle potential errors here
                        return;
                    }

                    if (value != null) {
                        // Convert Firestore documents to Notification objects
                        List<Notification> list = value.toObjects(Notification.class);
                        adapter.setNotifications(list);
                    }
                });
    }
}
