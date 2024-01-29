package com.example.opensourcepoe_part_2;

public class UserData {

    public String username;
    public String password;
    public String email;

    // Default constructor required for Firebase
    public UserData() {
    }

    public UserData(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
