package com.jiat.travelraapp.model;
import java.io.Serializable;

public class Vehicle implements Serializable {
    private String name;        // e.g., "Standard Sedan"
    private String type;        // e.g., "Sedan" or "SUV"
    private String imageUrl;    // URL from Travel Ra fleet
    private double pricePerDay; // Price for one day
    private double pricePerKm;  // Used for fare calculation
    private int capacity;       // Number of passengers
    private double baseFare;    // Starting price for trip

    public Vehicle() {} // Required for Firestore

    // Setters (Required for Firebase to populate the object correctly)
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setPricePerDay(double pricePerDay) { this.pricePerDay = pricePerDay; }
    public void setPricePerKm(double pricePerKm) { this.pricePerKm = pricePerKm; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setBaseFare(double baseFare) { this.baseFare = baseFare; }

    // Getters
    public String getName() { return name; }
    public String getType() { return type; }
    public String getImageUrl() { return imageUrl; }
    public double getPricePerDay() { return pricePerDay; }
    public double getPricePerKm() { return pricePerKm; }
    public int getCapacity() { return capacity; }
    public double getBaseFare() { return baseFare; }
}
