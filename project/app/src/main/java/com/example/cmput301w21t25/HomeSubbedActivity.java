package com.example.cmput301w21t25;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;

public class HomeSubbedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        FB_FetchSubscriptionsKeys(userID);
        finish();
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
                                            subscriptionList.add(binExp);
                                            Log.d("YA-DB: ", "SearchResults " + subscriptionList.get(0).getName());
                                            break;
                                        case "count":
                                            final CountExperiment countExp = document.toObject(CountExperiment.class);
                                            subscriptionList.add(countExp);
                                            break;
                                        case "non-neg-count":
                                            NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                            subscriptionList.add(nnCountExp);
                                            break;
                                        case "measurement":
                                            MeasurementExperiment mesExp = document.toObject(MeasurementExperiment.class);
                                            subscriptionList.add(mesExp);
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
}
