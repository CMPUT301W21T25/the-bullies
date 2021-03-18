package com.example.cmput301w21t25;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    FloatingActionButton incrementButton;
    TextView countDisplay;

    private String userID;
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");

        //Need to pass experiment ID to access title, description, etc. to pass to toolbar

        trialHeader = findViewById(R.id.countExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_count_button);
        incrementButton = findViewById(R.id.increment_count_button);
        countDisplay = findViewById(R.id.trial_count);

        //On click, increment the value stored in count, displayed in countDisplay
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count += 1;
                countDisplay.setText(Integer.toString(count));
            }
        });

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent return to list view and add to trial list
            }
        });
    }
}
