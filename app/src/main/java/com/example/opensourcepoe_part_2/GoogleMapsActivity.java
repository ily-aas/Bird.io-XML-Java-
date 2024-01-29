package com.example.opensourcepoe_part_2;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.opensourcepoe_part_2.databinding.ActivityGoogleMaps2Binding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    String keyAPI = "f7f4bcgo5940" , currentCountryCode, apiMapDisatnce , Maps_CurrentUser;
    private GoogleMap mMap;
    private ArrayList<Marker> markerList = new ArrayList<>();
    private ActivityGoogleMaps2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMaps2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("PassedLatitude"
                , 0.0);
        double longitude = intent.getDoubleExtra("PassedLongitude"
                , 0.0);
        apiMapDisatnce = intent.getStringExtra("apiDistance");

        try {

            Geocoder geocoder = new Geocoder(this);
            Address[] addressArray = null; // android has a default Address Object, for which we create and array of address objects.
            addressArray = geocoder.getFromLocation(latitude, longitude, 1).toArray(new Address[0]); //
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

                getHotspotsInRegion(latitude,longitude, keyAPI);

            } else {
                //No address found for given coordinates
            }

            // openMapsActivity(doubleLatitude, doubleLongitude);

        } catch(IOException e){
            e.printStackTrace();
        }

        // Add a marker in current location and move the camera
        float hueColor = BitmapDescriptorFactory.HUE_GREEN;
        LatLng currentLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(hueColor)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        getCurrentUser( latitude, longitude);

    }

    public void getHotspotsInRegion(double Lat, double Long, String APIkey) {


        //API rules require the Lat and Long to be rounded off to 2 decimal points
        double doubleLongitude = Math.round(Long * 100.0) / 100.0;;
        double doubleLatitude  = Math.round(Lat * 100.0) / 100.0;;


        // country = currentCountryCode;
        String url = "https://api.ebird.org/v2/ref/hotspot/geo?lat=" + doubleLatitude + "&lng="+  doubleLongitude + "&fmt=json &back=4 &dist=" + apiMapDisatnce;


        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response here
                        Log.d("API Response", response);

                        Gson gson = new Gson();
                        BirdDataAPI[] observations = gson.fromJson(response, BirdDataAPI[].class);

                        for (BirdDataAPI observation : observations) {


                            LatLng hotspotLocation = new LatLng(observation.getLat(), observation.getLng());
                            mMap.addMarker(new MarkerOptions().position(hotspotLocation).title(observation.getLocName()));


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("API Error", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                // Add your request headers here
                headers.put("x-ebirdapitoken", APIkey);
                return headers;
            }
        };

        // Create a RequestQueue (assuming you have already initialized this in your application)
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Add the request to the queue to execute it
        requestQueue.add(req);
    }

/*public void addObservations(String user){

    DatabaseReference observationsRef = FirebaseDatabase.getInstance().getReference("Observations");

    Query query = observationsRef.orderByChild("currentUser").equalTo(user);

    query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                BirdData birdData = snapshot.getValue(BirdData.class);

                if (birdData != null) {
                    // Access the data for each observation
                    Log.d("Firebase", "Bird Name: " + birdData.birdName);
                    Log.d("Firebase", "Latitude: " + birdData.birdLatitude);
                    Log.d("Firebase", "Longitude: " + birdData.birdLongitude);
                    // ... other properties
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("Firebase", "Read failed", databaseError.toException());
        }
    });*/


    /*FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
    DatabaseReference Ref = fireDatabase.getReference();

    Ref.child("Observations").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
        @Override
        public void onSuccess(DataSnapshot dataSnapshot) {
            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                // Get the value of each child node and store it in variables
                String childKey = childSnapshot.getKey();
                BirdData birdDataObject = childSnapshot.getValue(BirdData.class);

                if (birdDataObject != null) {

                    if (user.equals(birdDataObject.currentUser)) {
                            //double lat = 37.421998; // Example latitude
                            // double lng = -122.084; // Example longitude

                            // Earth’s radius in meters
                            double R = 6378137.0;

                            // Random offsets in meters (between 3 and 5)
                            Random random = new Random();
                            double distanceNorth = random.nextDouble() * (5 - 3) + 3;
                            double distanceEast = random.nextDouble() * (5 - 3) + 3;

                            // Coordinate offsets in radians
                            double dLat = distanceNorth / R;
                            double dLon = distanceEast / (R * Math.cos(Math.PI * birdDataObject.birdLatitude / 180));

                            // New coordinates
                            double newLat = birdDataObject.birdLatitude + dLat * 180 / Math.PI;
                            double newLng = birdDataObject.birdLongitude + dLon * 180 / Math.PI;

                            float hueColor = BitmapDescriptorFactory.HUE_BLUE;
                            LatLng hotspotLocation = new LatLng(newLat, newLng);
                            mMap.addMarker(new MarkerOptions().position(hotspotLocation).title(birdDataObject.birdName).icon(BitmapDescriptorFactory.defaultMarker(hueColor)));

                        }
                }
            }

        }
    });*/

    public void getCurrentUser(double lat, double lng){

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
                        Maps_CurrentUser = userDataObject.username;
                       // addObservations(Maps_CurrentUser);
                    }else{
                        //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

}