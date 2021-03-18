package com.example.cmput301w21t25;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductBinomialTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button successButton;
    Button failureButton;

    private String userID;
    private Boolean result;

    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");

        //Need to pass experiment ID to access title, description, etc. to pass to toolbar

        trialHeader = findViewById(R.id.binomialExperimentInfo);
        successButton = findViewById(R.id.binomialSuccessButton);
        failureButton = findViewById(R.id.binomialFailureButton);

        //On click, increment the value stored in count, displayed in countDisplay
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = true;
                //Call function that creates trial with result and switches activity
            }
        });

        //On click, confirm trial, return to trial list view
        failureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = false;
                //Call function that creates trial with result and switches activity
            }
        });
    }
}
