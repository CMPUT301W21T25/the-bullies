package com.example.cmput301w21t25.activities_trials;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConductBinomialTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button successButton;
    Button failureButton;
    TextView description;

    private String userID;
    private Experiment trialParent;
    private Boolean result;

    TrialManager trialManager;
    Bundle experiment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_binomial_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        //Pass experiment object to access attributes
        experiment = getIntent().getBundleExtra("EXP_BUNDLE");
        trialParent = (Experiment) experiment.getSerializable("TRIAL_PARENT");
        //Set up trial manager
        trialManager = new TrialManager();

        trialHeader = findViewById(R.id.binomialExperimentInfo);
        successButton = findViewById(R.id.binomialSuccessButton);
        failureButton = findViewById(R.id.binomialFailureButton);
        description = findViewById(R.id.binomialExpDescription);

        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        //On click, increment the value stored in count, displayed in countDisplay
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = true;
                //Call function that creates trial with result and switches activity
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, result, trialParent);
                /*
                Intent switchScreen = new Intent(HomeOwnedActivity.this, HomeSubbedActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                startActivity(switchScreen);
                 */
            }
        });

        //On click, confirm trial, return to trial list view
        failureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = false;
                //Call function that creates trial with result and switches activity
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, result, trialParent);
            }
        });
    }
}
