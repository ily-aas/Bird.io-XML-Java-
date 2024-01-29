package com.example.opensourcepoe_part_2;

public class BirdData {

    public String birdName;
    public String birdLocation;
    public int birdCount;

    public String currentUser;

    // Default constructor required for Firebase
    public BirdData() {
    }

    public BirdData(String name, String location, int count, String User) {
        this.birdName = name;
        this.birdLocation = location;
        this.birdCount = count;
        this.currentUser = User;
    }

}
