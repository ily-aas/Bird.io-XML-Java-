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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mapbox.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class homeActivity extends AppCompatActivity implements LocationListener {

    private boolean isMapActivityStarted = false;
    private LocationManager myLocationManager;
    String latitude,longitude,apiDisatnce, currentCountryCode, keyAPI = "f7f4bcgo5940";
    double doubleLatitude, doubleLongitude;
    private MapView mapView;

    //Variables to handle API
    String api_birdName;
    double api_birdLat, api_birdLong;

   // List<homeActivity> birdHotspotLocations = new ArrayList<>(String name, double lati, double longi);

    //String hotspots50km = "https://api.ebird.org/v2/ref/hotspot/geo?lat=" + latitude + "&lng=" + longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ContextCompat.checkSelfPermission(homeActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(homeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else{}

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userRef = firebaseDatabase.getReference("Settings/MapDistance");

        userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String selectedValue = dataSnapshot.getValue(String.class);
                    // Use the retrieved value here
                    if (selectedValue != null) {
                        // Do something with the retrieved value
                        apiDisatnce = selectedValue;
                    }
                } else {
                    // The data doesn't exist or is null
                }
            }
        });

    }


    //Current Location

    @Override
    public void onLocationChanged( Location location) {

        int count = 0;

        latitude = Double.toString(location.getLatitude());
        this.doubleLatitude = location.getLatitude();

        longitude = Double.toString(location.getLongitude());
        this.doubleLongitude = location.getLongitude();

        //Toast.makeText(this, " Lat: " + doubleLatitude+ "\n Long: " + doubleLongitude, Toast.LENGTH_LONG).show();

        try {

            Geocoder geocoder = new Geocoder(this);
            Address[] addressArray = null; // android has a default Address Object, for which we create and array of address objects.
            addressArray = geocoder.getFromLocation(doubleLatitude, doubleLongitude, 1).toArray(new Address[0]); //
            // we request only one address (1) based on the provided coordinates and convert it to an array.

            if (addressArray != null && addressArray.length > 0) {
                Address address = addressArray[0]; // Get the first address (most accurate)
                String fullAddress = address.getAddressLine(0); // Full address
                String city = address.getLocality(); // City
                String state = address.getAdminArea(); // State
                String country = address.getCountryName(); // Country
                String postalCode = address.getPostalCode(); // Postal code
                currentCountryCode = address.getCountryCode();//CountryCode

                // Toast.makeText(getApplicationContext(), "the full address is: \n"+fullAddress, Toast.LENGTH_LONG).show();

                //getHotspotsInRegion(currentCountryCode, keyAPI);

            } else {
                //No address found for given coordinates
            }


        } catch(IOException e){
            e.printStackTrace();
        }

        openMapsActivity(doubleLatitude, doubleLongitude);

    }

    //Current Location
    @SuppressLint("MissingPermission") // Suppress warnings to prevent errors since I already made sure the permissions are granted
    public void getLocation(){
        try {
            myLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, homeActivity.this);

        }catch(Exception e){
            Toast.makeText(this, "Error getting current location", Toast.LENGTH_SHORT).show();
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

    //Map
    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            stopLocationUpdates();
            mapView.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            stopLocationUpdates();
            mapView.onDestroy();
        }
    }

    private void openMapsActivity(double latitude_, double longitude_){

        getLocation();

        Intent intent = new Intent(this, GoogleMapsActivity.class);
        intent.putExtra("PassedLatitude"
                , latitude_);
        intent.putExtra("PassedLongitude"
                , longitude_);
        intent.putExtra("apiDistance"
                , apiDisatnce);

        startActivity(intent);
    }

    //Buttons

    public void navToMap(View view){

        // getLocation();
        //getHotspotsInRegion(currentCity, keyAPI);
        openMapsActivity(doubleLatitude,doubleLongitude);

    }

    public void navToSettings(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, settings.class);

        // Start the new activity
        startActivity(intent);

    }

    public void toObservations(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, Observations.class);

        // Start the new activity
        startActivity(intent);

    }

    public void toProfile(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, UserProfile.class);

        // Start the new activity
        startActivity(intent);

    }

    public void toCamera(View view){
        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, captureImage.class);

        // Start the new activity
        startActivity(intent);
    }

    private void stopLocationUpdates() {
        if (myLocationManager != null) {
            myLocationManager.removeUpdates(this);
        }
    }

}