package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class captureImage extends AppCompatActivity {


    String CI_CurrentUser;
    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;
    List<Bitmap> imageList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        camera_open_id= findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
        });
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //Add it to List to display
            imageList.add(photo);
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);

           /* FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference userRef = firebaseDatabase.getReference();

            userRef.child("CurrentUser").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Get the value of each child node and store it in variables
                        String childKey = childSnapshot.getKey();
                        UserData userDataObject = childSnapshot.getValue(UserData.class);

                        if (userDataObject != null) {
                            CI_CurrentUser = userDataObject.username;

                            //addImageToDB(CI_CurrentUser, photo);
                        }else{
                            //Toast.makeText(getApplicationContext(), " No Details Found ", Toast.LENGTH_LONG).show();
                        }
                    }
                    getImages(CI_CurrentUser);
                }
            });*/


        }
    }

    public void showImages(View view){

        ListView listView = findViewById(R.id.imgListView);
        imageListAdapter I_ListAdapter = new imageListAdapter(imageList, this);
        listView.setAdapter(I_ListAdapter);



    }

    public void addImageToDB(String user, Bitmap pic){


        Random random = new Random();
        int idNum = random.nextInt(900_000_000) + 100_000_000;


        // Create a Firebase object to save user data
        imageModel imgModel = new imageModel(user, pic);

        FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = fireDatabase.getReference("Images").child("IMG" + idNum);

        myRef.setValue(imgModel);
    }

    public void getImages(String u_current){

        FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
        DatabaseReference Ref = fireDatabase.getReference();

        Ref.child("Observations").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // Get the value of each child node and store it in variables
                    String childKey = childSnapshot.getKey();
                    imageModel imgObject = childSnapshot.getValue(imageModel.class);

                    if (imgObject != null) {

                        if (u_current.equals(imgObject.getCurrentUsr())) {


                        }
                    }
                }
            }
        });

    }
}