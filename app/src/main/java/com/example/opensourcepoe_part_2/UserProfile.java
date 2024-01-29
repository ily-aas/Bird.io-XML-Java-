package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    List<BirdData> observationList = new ArrayList<>();
    String UP_CurrentUser;
    TextView textObsCount, textBirdCount, Recruit,Novice,Expert;
    int birdCount = 0;
    int observationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textObsCount = findViewById(R.id.noOfObservations);
        textBirdCount = findViewById(R.id.speciesCount);
        Recruit = findViewById(R.id.recruitCompleted);
        Novice = findViewById(R.id.noviceCompleted);
        Expert = findViewById(R.id.expertCompleted);

        getCurrentUser();

    }

    public void returnBtn(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, homeActivity.class);

        // Start the new activity
        startActivity(intent);
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
                        UP_CurrentUser = userDataObject.username;
                        observationCount(UP_CurrentUser);
                    }else{
                        //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    public void observationCount(String currentUser){

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

                        if (currentUser.equals(birdDataObject.currentUser)) {

                            observationList.add(birdDataObject);
                            observationCount = observationList.size();
                            birdCount = birdCount + birdDataObject.birdCount;
                        }
                    }
                }
                textObsCount.setText(String.valueOf(observationList.size()));
                textBirdCount.setText(String.valueOf(birdCount));

                AchievementsCheck(observationCount);
            }
        });
    }

    public void AchievementsCheck(int obCount){

        if (obCount > 0){
            // set Acievement 1 colour to green
            Recruit.setTextColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_vanillagreenA700));
        }

        if (obCount > 4){
            // set Acievement 2 colour to green
            Novice.setTextColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_vanillagreenA700));
        }

        if (obCount > 9){
            // set Acievement 3 colour to green
            Expert.setTextColor(getResources().getColor(com.google.android.libraries.places.R.color.quantum_vanillagreenA700));
        }

    }
}