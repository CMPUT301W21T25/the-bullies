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

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.adapters.CustomListExperiment;
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

public class HomeSubbedActivity extends AppCompatActivity {

    private ListView subbedExperimentsList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ArrayList<Experiment> subbedExperiments;
    private FloatingActionButton browseButton;


    private float x1;
    private float x2;
    private float y1;
    private float y2;

    String userID;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);

        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        FB_FetchSubscriptionsKeys(userID);
        //finish();

        browseButton = findViewById(R.id.exp_search_button);


        subbedExperimentsList = findViewById(R.id.subbed_experiment_list_view);
        subbedExperiments = new ArrayList<Experiment>();
        experimentAdapter = new CustomListExperiment(this, subbedExperiments);
        subbedExperimentsList.setAdapter(experimentAdapter);

        subbedExperimentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DK: ", "Position clicked = " + position);
                Experiment experiment = (Experiment) subbedExperimentsList.getItemAtPosition(position);
                Intent viewExp = new Intent(HomeSubbedActivity.this, ViewExperimentActivity.class);

                Bundle expBundle = new Bundle();
                expBundle.putSerializable("EXP_OBJ", experiment);

                viewExp.putExtra("USER_ID", userID);
                viewExp.putExtra("EXP_BUNDLE", expBundle);

                startActivity(viewExp);
            }
        });

        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchScreen = new Intent(HomeSubbedActivity.this, SearchActivity.class);
                switchScreen.putExtra("USER_ID", userID);
                startActivity(switchScreen);
            }
        });

        //Prevent listview from eating onTouchEvent
        subbedExperimentsList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
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

                if (x1 < (x2)) {
                    Intent switchScreen = new Intent(HomeSubbedActivity.this, HomeOwnedActivity.class);
                    switchScreen.putExtra("USER_ID", userID);
                    startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String>subscriptionKeys = new ArrayList<String>();
    ArrayList<Experiment>subscriptionList = new ArrayList<Experiment>();
    public void FB_FetchSubscriptionsKeys(String id){
        subscriptionKeys.clear();
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        subscriptionKeys = (ArrayList<String>) document.getData().get("subscriptions");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + subscriptionKeys);
                        FB_FetchSubscriptions(subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches subscribed experiments
    public void FB_FetchSubscriptions(ArrayList<String> subscriptionKeys){
        subscriptionList.clear();//<------------------------------------------------ARRAY OF EXPERIMENTS THAT ARE FETCHED
        if(subscriptionKeys.isEmpty()==false){
            for (String key : subscriptionKeys) {
                DocumentReference docRef = db.collection("Experiments").document(key);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String type = (String)document.getData().get("type");
                                if(type!=null) {
                                    switch (type) {
                                        case "binomial":
                                            BinomialExperiment binExp = document.toObject(BinomialExperiment.class);
                                            binExp.setFb_id(document.getId());///TO DO: PUT THIS IN FUNCTION TO CUT REPEATED CODE
                                            subscriptionList.add(binExp);
                                            subbedExperiments.add(binExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            Log.d("YA-DB: ", "SearchResults " + subscriptionList.get(0).getName());
                                            break;
                                        case "count":
                                            final CountExperiment countExp = document.toObject(CountExperiment.class);
                                            countExp.setFb_id(document.getId());
                                            subscriptionList.add(countExp);
                                            subbedExperiments.add(countExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        case "nonnegative count":
                                            NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                            nnCountExp.setFb_id(document.getId());
                                            subscriptionList.add(nnCountExp);
                                            subbedExperiments.add(nnCountExp);
                                            experimentAdapter.notifyDataSetChanged();
                                            break;
                                        case "measurement":
                                            MeasurementExperiment mesExp = document.toObject(MeasurementExperiment.class);
                                            mesExp.setFb_id(document.getId());
                                            subscriptionList.add(mesExp);
                                            subbedExperiments.add(mesExp);
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
    public void viewSubbediButton(View view) {
        //switch to profileView, pass userId
        Intent intent = new Intent(HomeSubbedActivity.this, MyUserProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("prevScreen", "Subbed");
        startActivity(intent);
    }


}
