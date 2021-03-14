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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    public SearchManager searchManager = new SearchManager();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        FB_FetchExperimentList(userID);
        finish();
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
                    if (document.exists()&&(Boolean) document.getData().get("published")==true) {
                        subscriptionKeys = (ArrayList<String>) document.getData().get("ownedExperiments");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + subscriptionKeys);
                        FB_FetchOwned(subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }

    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches subscribed experiments
    public void FB_FetchOwned(ArrayList<String> subscriptionKeys) {
        experimentList.clear();
        if (subscriptionKeys.isEmpty() == false) {
            DocumentReference docRef = db.collection("Experiments").document();
            db.collection("Experiments")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists() && subscriptionKeys.contains(document.getId()) != true) {
                                        Experiment test = document.toObject(Experiment.class);
                                        experimentList.add(test);
                                        Log.d("YA-DB: ", "SearchResults " + experimentList);
                                        //inside here update the feilds and stuff
                                    }
                                    else{
                                        Log.d("YA-DB: ", "search failed ", task.getException());
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
