package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.CustomToolbar;
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
    private String userID;

    //Variables to access on touch events
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private int publishedTrials = 0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_owned);


        /*setup the custom toolbar!
        */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = getIntent().getStringExtra("USER_ID");
        FB_FetchOwnedKeys(userID);

        ownedExperimentsList = new ArrayList<Experiment>();
        ownedExperimentsListView = findViewById(R.id.owned_experiment_list);
        experimentAdapter = new CustomListExperiment(this, ownedExperimentsList);
        ownedExperimentsListView.setAdapter(experimentAdapter);

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

    //Toolbar Menu setup!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(HomeOwnedActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("userID", userID);
                user_settings.putExtra("prevScreen", "Owned");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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



    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> ownedKeys = new ArrayList<String>();
    /**
     * this fetches a list of keys of experiments the user is subscribed to and then calls FB_FetchOwned() on them.
     * @param id id of the user
     */
    public void FB_FetchOwnedKeys(String id){
        ownedKeys.clear();
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownedKeys = (ArrayList<String>) document.getData().get("ownedExperiments");
                        Log.d("DK: ", "DocumentSnapshot data: " + ownedKeys + "User ID: " + id);
                        FB_FetchOwned(ownedKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches subscribed experiments
    /**
     * Fetches list of owned experiments using the provided list of keys. It then updates experiment adapater
     * @param ownedKeys a list of keys of the experiments this user created
     */
    public void FB_FetchOwned(ArrayList<String> ownedKeys){
        ownedExperimentsList.clear();//<------------------------------------------------ARRAY OF EXPERIMENTS THAT ARE FETCHED
        if(!ownedKeys.isEmpty()){
            for (String key : ownedKeys) {
                DocumentReference docRef = db.collection("Experiments").document(key);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //ownedExperiments.add(document.toObject(Experiment.class));
                                //@Yalmaz I don't think we need this next line?
                                //Experiment test = document.toObject(Experiment.class);
                                //Log.d("YA-DB: ", "SearchResults " + test.getName());
                                String type = (String)document.getData().get("type");
                                Log.d("YA-DB: ", "testing");

                                if(type!=null) {
                                    switch (type) {
                                        case "binomial":
                                            //ArrayList<Experiment>test = new ArrayList<Experiment>();
                                            BinomialExperiment binExp = document.toObject(BinomialExperiment.class);
                                            binExp.setFb_id(document.getId());
                                            //binExp.setTrialKeys((ArrayList<String>)document.getData().get("trialKeys"));
                                            ownedExperimentsList.add(binExp);
                                            Log.d("YA-DB: ", "SearchResults " + ownedExperimentsList.get(0).getName());
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        case "count":
                                            final CountExperiment countExp = document.toObject(CountExperiment.class);
                                            countExp.setFb_id(document.getId());
                                            //countExp.setTrialKeys((ArrayList<String>)document.getData().get("trialKeys"));
                                            ownedExperimentsList.add(countExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        case "nonnegative count":
                                            NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                            nnCountExp.setFb_id(document.getId());
                                            //nnCountExp.setTrialKeys((ArrayList<String>)document.getData().get("trialKeys"));
                                            ownedExperimentsList.add(nnCountExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        case "measurement":
                                            MeasurementExperiment mesExp = document.toObject(MeasurementExperiment.class);
                                            mesExp.setFb_id(document.getId());
                                            //mesExp.setTrialKeys((ArrayList<String>)document.getData().get("trialKeys"));
                                            ownedExperimentsList.add(mesExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        default:
                                            Log.d("YA-DB: ", "this experiment was not assigned the correct class when it was uploaded so i dont know what class to make");
                                    }
                                }
                                //Experiment test = document.toObject(Experiment.class);
                                //Log.d("YA-DB: ", "SearchResults " + test.getName());
                                //inside here update the feilds and stuff
                            }
                        } else {
                            Log.d("YA-DB: ", "search failed ");
                        }
                    }
                });
            }
        }
    }

    /**
     * Is called when a user clicks on their profile image
     * Will switch to a profile view activity
     * Curtis
     * @param view
     */
//    public void viewOwnediButton(View view) {
//        //switch to profileView, pass userId
//        Intent intent = new Intent(HomeOwnedActivity.this, MyUserProfileActivity.class);
//        intent.putExtra("userID", userID);
//        intent.putExtra("prevScreen", "Owned");
//        startActivity(intent);
//    }

}
