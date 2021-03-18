package com.example.cmput301w21t25.activities_trials;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;

public class ConductMeasurementTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    EditText measurementDisplay;

    private String userID;
    private String measurementString;
    private Float measurement;

    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_measurement_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");

        //Need to pass experiment ID to access title, description, etc. to pass to toolbar

        trialHeader = findViewById(R.id.measurementExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_measurement_button);
        measurementDisplay = findViewById(R.id.measurementEntry);

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent return to list view and add to trial list
                measurementString = measurementDisplay.getText().toString();
                measurement = Float.parseFloat(measurementString);
            }
        });
    }
}
