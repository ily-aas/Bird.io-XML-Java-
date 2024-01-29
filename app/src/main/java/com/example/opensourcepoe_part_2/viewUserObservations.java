package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class viewUserObservations extends AppCompatActivity {

    ArrayList<viewUserObservations> birdList = new ArrayList<>();

     String DB_birdName, DB_birdLocation, VUO_CurrentUser;
     int DB_birdCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_observations);

        getCurrentUser();

    }

    public void navToPrevious(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, Observations.class);

        // Start the new activity
        startActivity(intent);

    }

    public void showList(){

        ListView listView = findViewById(R.id.listView);
        ItemListAdapter I_ListAdapter = new ItemListAdapter(birdList, this);
        listView.setAdapter(I_ListAdapter);

    }

    public void getObservations(String u_current){

        FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
        DatabaseReference Ref = fireDatabase.getReference();

        Ref.child("Observations").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the value of each child node and store it in variables
                    String childKey = childSnapshot.getKey();
                    BirdData birdDataObject = childSnapshot.getValue(BirdData.class);

                    if (birdDataObject != null) {

                        if (u_current.equals(birdDataObject.currentUser)) {

                            viewUserObservations VUO = new viewUserObservations();

                            VUO.DB_birdName = birdDataObject.birdName;
                            VUO.DB_birdLocation = birdDataObject.birdLocation;
                            VUO.DB_birdCount = birdDataObject.birdCount;

                            birdList.add(VUO);
                        }
                    }
                }
                showList();
            }
        });

    }

    public void getCurrentUser(){
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
                        VUO_CurrentUser = userDataObject.username;
                        getObservations(VUO_CurrentUser);
                    }else{
                        //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}