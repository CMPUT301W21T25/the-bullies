package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListTrial;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * this activity is used to add new trials and view unpublished trials
 */
public class AddTrialActivity extends AppCompatActivity {

    ListView trialListView;
    ArrayAdapter<Trial> trialArrayAdapter;
    FloatingActionButton addTrialButton;

    private String userID;
    private Experiment exp;
    private String expID;

    private TrialManager trialManager;
    private  ArrayList<Trial> trialList = new ArrayList<Trial>();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_trial_list);

        trialManager = new TrialManager();

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        expID = exp.getFb_id();
        //FB_FetchTrialKeys(expID,userID,exp);
        trialArrayAdapter = new CustomListTrial(this, trialList);
        trialManager.FB_UpdateTrialAdapter(exp,trialArrayAdapter,trialList);
        addTrialButton = findViewById(R.id.trial_create_button);
        trialListView = findViewById(R.id.add_trial_list);


        trialListView.setAdapter(trialArrayAdapter);

        trialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publishTrial(position);
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches a conduct trial activity based on type of experiment
                Class className;
                Intent switchScreen = null;
                switch (exp.getType()) {
                    case "count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductCountTrialActivity.class);
                        //This line makes sure that this activity is not saved in the history stack
                        switchScreen.addFlags(switchScreen.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "nonnegative count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductNonnegativeCountTrialActivity.class);
                        //This line makes sure that this activity is not saved in the history stack
                        switchScreen.addFlags(switchScreen.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "binomial":
                        switchScreen = new Intent(AddTrialActivity.this, ConductBinomialTrialActivity.class);
                        //This line makes sure that this activity is not saved in the history stack
                        switchScreen.addFlags(switchScreen.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "measurement":
                        switchScreen = new Intent(AddTrialActivity.this, ConductMeasurementTrialActivity.class);
                        //This line makes sure that this activity is not saved in the history stack
                        switchScreen.addFlags(switchScreen.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                }
            }
        });

        //finish();
    }

    /**
     * this method defines the behaviour of the addTrialButton
     * @param view
     */
    public void addTrialiButton(View view) {
        //switch to profileView, pass userId
        Intent intent = new Intent(AddTrialActivity.this, MyUserProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("prevScreen", "AddTrial");
        //bundle experiment to return to

        intent.putExtra("TRIAL_PARENT", exp);
        startActivity(intent);

    }

    /**
     * Published a trial at the position clicked on in the trial list view
     * @param position
     * The index of the trial you want to publish in the list
     */
    public void publishTrial(int position) {
        Trial temp = trialList.remove(position);
        temp.setPublished(true);
        trialManager.FB_UpdatePublished(true, temp.getTrialId());
        trialArrayAdapter.notifyDataSetChanged();
    }

}
