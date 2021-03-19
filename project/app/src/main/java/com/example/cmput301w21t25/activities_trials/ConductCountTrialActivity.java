package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    FloatingActionButton incrementButton;
    TextView countDisplay;
    TextView description;

    private Experiment trialParent;
    private String userID;
    private int count = 0;

    private TrialManager trialManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        //Need to pass experiment ID to access title, description, etc. to pass to toolbar
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        trialManager = new TrialManager();

        trialHeader = findViewById(R.id.countExperimentInfo);

        submitTrialButton = findViewById(R.id.submit_trial_count_button);
        incrementButton = findViewById(R.id.increment_count_button);
        countDisplay = findViewById(R.id.trial_count);
        description = findViewById(R.id.countExperimentInfo);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

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
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, count, trialParent);
                //Intent return to list view and add to trial list
                Intent switchScreen = new Intent(ConductCountTrialActivity.this, AddTrialActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", trialParent);
                startActivity(switchScreen);
            }
        });
    }
}
