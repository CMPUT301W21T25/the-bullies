package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;

public class ConductMeasurementTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    EditText measurementDisplay;

    private Experiment trialParent;
    private String userID;
    private String measurementString;
    private Float measurement;

    private TrialManager trialManager;

    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_measurement_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        //Initialize TrialManager
        trialManager = new TrialManager();

        //Need to pass experiment ID to access title, description, etc. to pass to toolbar

        trialHeader = findViewById(R.id.measurementExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_measurement_button);
        measurementDisplay = findViewById(R.id.measurementEntry);

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                measurementString = measurementDisplay.getText().toString();
                measurement = Float.parseFloat(measurementString);
                //Create the doc form of the trial in the database to call later
                trialManager.FB_CreateMeasurementTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, measurement, trialParent);
                //Intent return to list view and add to trial list
                Intent switchScreen = new Intent(ConductMeasurementTrialActivity.this, AddTrialActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", trialParent);
                startActivity(switchScreen);
            }
        });
    }
}
