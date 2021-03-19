package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductNonnegativeCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    EditText countDisplay;
    TextView description;

    private Experiment trialParent;
    private TrialManager trialManager;
    private String userID;
    private String countString;
    private int count;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_nonnegative_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        //Initialize TrialManager
        trialManager = new TrialManager();

        //We likely need to fetch what the experiment is out of? E.g., out of ___ eggs in a carton,
        //how many cracked when you dropped the carton?

        trialHeader = findViewById(R.id.nonnegativeCountExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_nonnegative_count_button);
        countDisplay = findViewById(R.id.nonnegativeCountEntry);
        description = findViewById(R.id.nonnegativeCountExperimentInfo);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent return to list view and add to trial list
                countString = countDisplay.getText().toString();
                count = Integer.parseInt(countString);
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, count, trialParent);
                Intent switchScreen = new Intent(ConductNonnegativeCountTrialActivity.this, AddTrialActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", trialParent);
                startActivity(switchScreen);
            }
        });
    }
}
