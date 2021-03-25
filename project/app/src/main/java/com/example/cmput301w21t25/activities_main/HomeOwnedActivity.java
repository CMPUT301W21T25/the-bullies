package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.FirestoreCallback;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.CreateExperimentActivity;
import com.example.cmput301w21t25.activities_experiments.ViewCreatedExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.managers.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * this activity shows a list of all the experiments this user has created
 */
public class HomeOwnedActivity extends AppCompatActivity {

    private ListView ownedExperimentsListView;
    private ArrayList<Experiment> ownedExperimentsList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ExperimentManager experimentManager = new ExperimentManager();
    private String userID;

    //Variables to access on touch events
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private int publishedTrials = 0;
    private ArrayList<String>key = new ArrayList<String>();




    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_owned);

        userID = getIntent().getStringExtra("USER_ID");

        ownedExperimentsList = new ArrayList<Experiment>();
        ownedExperimentsListView = findViewById(R.id.owned_experiment_list);
        experimentAdapter = new CustomListExperiment(this, ownedExperimentsList);
        ownedExperimentsListView.setAdapter(experimentAdapter);

        /////////////////////////////////////////////
        UserManager userManager = new UserManager();
        ArrayList<String> keys = new ArrayList<String>();
        experimentManager.FB_UpdateOwnedExperimentAdapter(userID,experimentAdapter,ownedExperimentsList);





        final FloatingActionButton createExperimentButton = findViewById(R.id.exp_create_button);

        //Prevent ListView from eating onTouchEvent
        ownedExperimentsListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        ownedExperimentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DK: ", "Position clicked = " + position);
                Experiment experiment = (Experiment) ownedExperimentsListView.getItemAtPosition(position);
                //FB_FetchPublishedTrials(experiment);

                int pubTrials = publishedTrials;
                experiment.setCurrentNumTrials(pubTrials);
                Log.d("pubTrials", String.valueOf(pubTrials));
                Log.d("getTrials", String.valueOf(experiment.getCurrentNumTrials()));



                Intent viewExp = new Intent(HomeOwnedActivity.this, ViewCreatedExperimentActivity.class);

                Bundle expBundle = new Bundle();
                expBundle.putSerializable("EXP_OBJ", experiment);

                viewExp.putExtra("USER_ID", userID);
                viewExp.putExtra("EXP_BUNDLE", expBundle);

                startActivity(viewExp);
            }
        });

        // OnClickListener to transfer user to Create Experiment Activity
        createExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newExp = new Intent(HomeOwnedActivity.this, CreateExperimentActivity.class);
                newExp.putExtra("USER_ID", userID);
                startActivity(newExp);
            }
        });
    }

    //Screen switching
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float y = (y1 - y2);
                float x = (x1 - x2);

                //To deal with sensitivity so scrolling doesn't switch screens
                if (Math.abs(y) > Math.abs(x)) {
                    return false;
                }

                if (x1 > (x2)) {
                    Intent switchScreen = new Intent(HomeOwnedActivity.this, HomeSubbedActivity.class);
                    switchScreen.putExtra("USER_ID", userID);
                    startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    /**
     * Is called when a user clicks on their profile image
     * Will switch to a profile view activity
     * Curtis
     * @param view
     */
    public void viewOwnediButton(View view) {
        //switch to profileView, pass userId
        Intent intent = new Intent(HomeOwnedActivity.this, MyUserProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("prevScreen", "Owned");
        startActivity(intent);
    }

}
