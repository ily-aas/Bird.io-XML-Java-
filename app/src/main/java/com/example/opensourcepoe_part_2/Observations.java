package com.example.opensourcepoe_part_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Observations extends AppCompatActivity implements LocationListener {


    String birdName, birdLocationName, currentUser;
    int birdCount;
    String obsLatitude,obsLongitude, obsCurrentCountry;

    double obsDoubleLatitude, obsDoubleLongitude;

    private String birdLocationCountry; // Declare a member variable

    EditText textBirdName, textBirdLocationName, textBirdCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observations);

        textBirdName = findViewById(R.id.birdName);
        textBirdLocationName = findViewById(R.id.birdLocationName);
        textBirdCount = findViewById(R.id.birdCount);

        if (ContextCompat.checkSelfPermission(Observations.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Observations.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else{}

        getLocation();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference();

        userRef.child("CurrentUser").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the value of each child node and store it in variables
                    String childKey = childSnapshot.getKey();
                    UserData userDataObject = childSnapshot.getValue(UserData.class);

                    if (userDataObject != null) {
                        currentUser = userDataObject.username;
                    }else{
                        //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void submitBirdObservation(View view ){


        birdName = textBirdName.getText().toString();
        birdLocationName = birdLocationCountry;
        String birdNumber = textBirdCount.getText().toString();

        if (birdName.equals("") || birdLocationName.equals("") || birdNumber.equals("")){

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        }else{

            birdCount = Integer.parseInt(birdNumber);

                // Boolean validData = countChars(birdName,birdLocationName,birdCount);
                Random random = new Random();
                int idNum = random.nextInt(900_000_000) + 100_000_000;

                // Create a Firebase object to save user data
                BirdData birdData = new BirdData(birdName, birdLocationName, birdCount, currentUser);


                FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
                DatabaseReference myRef = fireDatabase.getReference("Observations").child("Bird" + idNum);

                myRef.setValue(birdData);


                Toast.makeText(this, "Observation Saved", Toast.LENGTH_LONG).show();
                textBirdName.setText("");
                textBirdCount.setText("");
        }

    }

    @SuppressLint("MissingPermission")// Suppress warnings to prevent errors since I already made sure the permissions are granted
    public void getLocation(){
        try {
            LocationManager myLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, Observations.this);

        }catch(Exception e){
            Toast.makeText(this, "Error getting current location", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged( Location location) {
        obsLatitude = Double.toString(location.getLatitude());
        this.obsDoubleLatitude = location.getLatitude();

        obsLongitude = Double.toString(location.getLongitude());
        this.obsDoubleLongitude = location.getLongitude();

        //Toast.makeText(this, " Lat: " + obsDoubleLatitude+ "\n Long: " + obsDoubleLongitude, Toast.LENGTH_LONG).show();

        try {

            Geocoder geocoder = new Geocoder(this);
            Address[] addressArray = null; // android has a default Address Object, for which we create and array of address objects.
            addressArray = geocoder.getFromLocation(obsDoubleLatitude, obsDoubleLongitude, 1).toArray(new Address[0]); //
            // we request only one address (1) based on the provided coordinates and convert it to an array.

            if (addressArray != null && addressArray.length > 0) {
                Address address = addressArray[0]; // Get the first address (most accurate)
                String fullAddress = address.getAddressLine(0); // Full address
                String city = address.getLocality(); // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country
                String postalCode = address.getPostalCode(); // Postal code

                //Toast.makeText(getApplicationContext(), "the full address is: \n"+fullAddress, Toast.LENGTH_LONG).show();

                birdLocationCountry = country + ", " + state + ", " + city;
                textBirdLocationName.setText(birdLocationCountry);

            } else {
                //No address found for given coordinates
            }

        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Handle status changes if needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Handle provider enabled event if needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Handle provider disabled event if needed
    }

    public boolean countChars(String Name, String Location, int Count){

        char[] charArrayName = Name.toCharArray();
        int nameLength = charArrayName.length;

        char[] charArrayLocation = Location.toCharArray();
        int locationLength = charArrayLocation.length;

        if(nameLength < 20){
            if(locationLength < 50){
                if (Count > 0){

                    return true;

                }else{
                    Toast.makeText(this, "Bird count cannot be less than or equal to 0", Toast.LENGTH_LONG).show();
                    return false;
                }
            }else{
                Toast.makeText(this, "Location length too long", Toast.LENGTH_LONG).show();
                return false;
            }
        }else{

            Toast.makeText(this, "Name length too long", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public void returnToHome(View view){


        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, homeActivity.class);

        // Start the new activity
        startActivity(intent);

    }

    public void toViewObs(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, viewUserObservations.class);

        // Start the new activity
        startActivity(intent);


    }

    public void navToProfile(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, UserProfile.class);

        // Start the new activity
        startActivity(intent);

    }
}