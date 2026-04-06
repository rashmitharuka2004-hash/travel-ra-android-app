package com.jiat.travelraapp.model;
import java.io.Serializable;

public class TourPackage implements Serializable {
    private String id;
    private String title;
    private String duration;
    private String price;
    private String imageUrl;
    private String description;
    private String rating;
    private String places;

    public TourPackage() {} // Required for Firebase

    // ADD THIS METHOD TO FIX THE ERROR
    public void setId(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDuration() { return duration; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getRating() { return rating; }
    public String getPlaces() { return places; }
}
