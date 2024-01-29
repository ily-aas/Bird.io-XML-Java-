package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class login_Activity extends AppCompatActivity {

    String Username, Password, DB_Username, DB_Password ;
    EditText textUsername, textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(login_Activity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(login_Activity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else{}

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        //Binding the UI to variables declared in class
        textUsername = findViewById(R.id.LoginUsernameEditText);
        textPassword = findViewById(R.id.LoginPasswordEditText);
    }

    public void submitLogForm(View view){

        getCredentials();

    }

    public void toSignUp(View view){


        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, MainActivity.class);

        // Start the new activity
        startActivity(intent);

    }

    public void HomeAct(){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, homeActivity.class);

        // Start the new activity
        startActivity(intent);


    }

    public void getCredentials(){

        // Get the text from the EditText and assign it to the Username variable

        Username = textUsername.getText().toString();
        Password = textPassword.getText().toString();

        Password = hashString(Password);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference();

        userRef.child("Credentials").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the value of each child node and store it in variables
                    String childKey = childSnapshot.getKey();
                    UserData userDataObject = childSnapshot.getValue(UserData.class);

                    if (userDataObject != null) {
                        DB_Username = userDataObject.username;
                        DB_Password = userDataObject.password;

                        if(DB_Username.equals(Username) && DB_Password.equals(Password)){
                            FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference currentUserRef = fireDatabase.getReference("CurrentUser").child("CurrentUserID");
                            currentUserRef.setValue(userDataObject);

                            HomeAct();
                            Toast.makeText(login_Activity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

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