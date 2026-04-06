package com.jiat.travelraapp.model;

public class Destination {
    private String title;
    private String description;
    private String category;
    private String imageUrl;

    // Required empty constructor for Firebase
    public Destination() {}

    public Destination(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }


    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}
