package com.example.cmput301w21t25;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public SearchManager searchManager = new SearchManager();
    ListView browseList;
    ArrayAdapter<Experiment> experimentArrayAdapter;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_browse_not_subbed);

        String userID;

        userID = getIntent().getStringExtra("USER_ID");

        browseList = findViewById(R.id.browse_experiment_list);
        experimentArrayAdapter = new CustomListExperiment(this, experimentList, userID);
        browseList.setAdapter(experimentArrayAdapter);

        FB_FetchExperimentList(userID);
        //finish();

    }

    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> subscriptionKeys = new ArrayList<String>();
    ArrayList<Experiment> experimentList = new ArrayList<Experiment>();



    public void FB_FetchExperimentList(String id) {
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
                        FB_FetchNotSubscribed(subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }

    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches experiments that are NOT subscribed AND published
    public void FB_FetchNotSubscribed(ArrayList<String> subscriptionKeys) {
        experimentList.clear();//<------------------------------------------------ARRAY OF EXPERIMENTS THAT ARE FETCHED
        if (subscriptionKeys.isEmpty() != false) {
            DocumentReference docRef = db.collection("Experiments").document();
            db.collection("Experiments")
                    .whereEqualTo("published", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //ArrayList<Experiment> test2 = new ArrayList<Experiment>();
                                    //test2.add(new BinomealExperiment());
                                    //test2.add(new Experiment());
                                    //Log.d("test",test2.get(1).getName());
                                    if (document.exists() && subscriptionKeys.contains(document.getId()) != true) {
                                        String type = (String)document.getData().get("type");
                                        if(type!=null){
                                            //calls the create trial(parent id, parent type)
                                            switch(type){
                                                case "binomial":
                                                    BinomialExperiment binExp = document.toObject(BinomialExperiment.class);
                                                    experimentList.add(binExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    Log.d("YA-DB: ", "SearchResults " + experimentList.get(0).getName());
                                                    break;
                                                case"count":
                                                    final CountExperiment countExp = document.toObject(CountExperiment.class);
                                                    experimentList.add(countExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    break;
                                                case "non-neg-count":
                                                    NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                                    experimentList.add(nnCountExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    break;
                                                case"measurement":
                                                    MeasurementExperiment mesExp = document.toObject(MeasurementExperiment.class);
                                                    experimentList.add(mesExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    break;
                                                default:
                                                    Log.d("YA-DB: ","this experiment was not assigned the correct class when it was uploaded so i dont know what class to make");
                                            }
                                        }
                                        //Experiment test = document.toObject(Experiment.class);
                                        //experimentList.add(test);
                                        //inside here update the feilds and stuff
                                    }
                                    else{
                                        Log.d("YA-DB: ", "this is already subbed to");
                                    }
                                }
                                //call search manager here
                                //searchManager.searchExperimentKeywords();
                            }
                        }
                    });
        }
    }
}
