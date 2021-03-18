package com.example.cmput301w21t25.activities_trials;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.experiments.Experiment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddTrialActivity extends AppCompatActivity {

    ListView browseList;
    ArrayAdapter<Experiment> experimentArrayAdapter;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        String userID;
        Experiment expID;
        userID = getIntent().getStringExtra("USER_ID");
        expID = (Experiment) getIntent().getSerializableExtra("EXP_ID");
        FB_FetchTrials(userID);
        //finish();

    }






    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> subscriptionKeys = new ArrayList<String>();
    ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
    public void FB_FetchTrials(String id) {
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
                        //FB_FetchNotSubscribed(subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
}
