package com.example.opensourcepoe_part_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.PropertyName;

public class settings extends AppCompatActivity {

    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
     String selectedValue;
     Switch metricSwitchButton,imperialSwitchButton;
     boolean isSwitchCheckedMetric, isSwitchCheckedImperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = findViewById(R.id.spinner);
        metricSwitchButton = findViewById(R.id.metricSwitch);
        imperialSwitchButton = findViewById(R.id.imperialSwitchBtn);

        //Populating the spinner
        String[] items = new String[]{"5", "10", "15"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);

        //Metric toggle button
        metricSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                imperialSwitchButton.setChecked(false);
                isSwitchCheckedImperial = false;

                isSwitchCheckedMetric = true;

            } else {
                // The switch isn't checked.
            }
        });

        //Imperial toggle button
        imperialSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                metricSwitchButton.setChecked(false);
                isSwitchCheckedMetric = false;

                isSwitchCheckedImperial = true;

            } else {
                // The switch isn't checked.
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Get the selected item from the Spinner
                selectedValue = spinner.getSelectedItem().toString();
                // You can use the selectedValue here or pass it to another method
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle situation when nothing is selected (if needed)
            }
        });
    }

   public void saveSettings(View view){

        if (isSwitchCheckedMetric == true){

            FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fireDatabase.getReference("Settings").child("Units");

            myRef.setValue("Metric");

        }else if (isSwitchCheckedImperial == true){
            FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = fireDatabase.getReference("Settings").child("Units");

            myRef.setValue("Imperial");
        }

       FirebaseDatabase fireDatabase = FirebaseDatabase.getInstance();
       DatabaseReference myRef = fireDatabase.getReference("Settings").child("MapDistance");

       myRef.setValue(selectedValue);

       Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();

   }

    public void navToHome(View view){

        // Create an Intent to navigate to the NewActivity
        Intent intent = new Intent(this, homeActivity.class);

        // Start the new activity
        startActivity(intent);

    }


}