package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;

/**
 * @author Eden
 * Conduct binomial trial activity, allows experimenter to increment count. On completion, sends
 * trial to database as doc, returns to add trial list view.
 */
public class ConductBinomialTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button successButton;
    Button failureButton;
    TextView description;

    private String userID;
    private Experiment trialParent;

    TrialManager trialManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_binomial_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        //Pass experiment object to access attributes
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        //Set up trial manager
        trialManager = new TrialManager();


        trialHeader = findViewById(R.id.binomialExperimentInfo);
        description = findViewById(R.id.binomialExpDescription);
        successButton = findViewById(R.id.binomialSuccessButton);
        failureButton = findViewById(R.id.binomialFailureButton);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        //On click, assign True or False value of Boolean Experiment
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call function that creates trial with result and switches activity
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, true, trialParent);
                Intent switchScreen = new Intent(ConductBinomialTrialActivity.this, AddTrialActivity.class);
                //Passes the parent Experiment back as it is needed in the add Trial list view
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", trialParent);
                startActivity(switchScreen);
            }
        });

        //On click, confirm trial, return to trial list view
        failureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call function that creates trial with result and switches activity
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, false, trialParent);
                Intent switchScreen = new Intent(ConductBinomialTrialActivity.this, AddTrialActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                switchScreen.putExtra("TRIAL_PARENT", trialParent);
                startActivity(switchScreen);
            }
        });
    }
}
