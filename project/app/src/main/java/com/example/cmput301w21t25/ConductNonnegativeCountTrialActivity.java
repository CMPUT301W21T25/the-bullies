package com.example.cmput301w21t25;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductNonnegativeCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    EditText countDisplay;

    private String userID;
    private String countString;
    private int count;

    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");

        //Need to pass experiment ID to access title, description, etc. to pass to toolbar

        //We likely need to fetch what the experiment is out of? E.g., out of ___ eggs in a carton,
        //how many cracked when you dropped the carton?

        trialHeader = findViewById(R.id.nonnegativeCountExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_nonnegative_count_button);
        countDisplay = findViewById(R.id.nonnegativeCountEntry);

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent return to list view and add to trial list
                countString = countDisplay.getText().toString();
                count = Integer.parseInt(countString);
            }
        });
    }
}
