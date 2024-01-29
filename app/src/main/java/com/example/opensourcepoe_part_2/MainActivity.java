package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//Databse
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//Random digits
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Password hashing
import java.io.*;
public class MainActivity extends AppCompatActivity {

    String Username, Password, Email;
    EditText textUsername, textPassword, textEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        //Binding the UI to variables declared in class
        textUsername = findViewById(R.id.editTextUsername);
        textPassword = findViewById(R.id.editTextPassword);
        textEmail = findViewById(R.id.editTextEmail);

    }

    public void submitForm(View view) {

        // Get the text from the EditText and assign it to the Username variable
        Username = textUsername.getText().toString();
        Password = textPassword.getText().toString();
        Email = textEmail.getText().toString();

        //Checks for empty fields
        if (Username.equals("") || Password.equals("") || Email.equals("")) {

            Toast.makeText(getApplicationContext(), "Please fill in all fields ", Toast.LENGTH_LONG).show();

        } else {


            Password = hashString(Password);

            Random random = new Random();
            int idNum = random.nextInt(900_000_000) + 100_000_000;

            // Create a Firebase object to save user data
            UserData userData = new UserData(Username, Password, Email);

            FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fireDatabase.getReference("Credentials").child("User" + idNum);

            myRef.setValue(userData);

            Toast.makeText(getApplicationContext(), "Account created successfully ", Toast.LENGTH_LONG).show();

            // Create an Intent to navigate to the NewActivity
            Intent intent = new Intent(this, login_Activity.class);

            // Start the new activity
            startActivity(intent);
        }
    }


    public void toLogin(View view) {

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, login_Activity.class);

        // Start the new activity
        startActivity(intent);

    }

    public static boolean isValidEmail(String email) {

        // Define a regular expression pattern for a basic email validation
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }

    public boolean countUsernameChars(String u_name) {

        char[] charArray = u_name.toCharArray();
        int length = charArray.length;

        //Username length cannot be more than 20 characters
        if (length > 20 || length < 8) {

            Toast.makeText(getApplicationContext(), "Username must be between 8-20 characters long", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }


    }

    public static boolean checkValidPassword(String p_word) {

        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(p_word);

        return m.matches();
    }

    public String hashString(String pword) {

        try {
            // Create SHA-256 hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(pword.getBytes());

            // Convert bytes to hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the NoSuchAlgorithmException
        }
        return ""; // In case of failure, a default value is returned
    }

}