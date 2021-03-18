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
        userID = getIntent().getStringExtra("USER_ID");
        Experiment exp = (Experiment) getIntent().getSerializableExtra("EXP_ID");
        String expID = exp.getFb_id();
        FB_FetchTrialKeys(expID,userID);
        //finish();

    }






    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> trialKeys = new ArrayList<String>();
    ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
    public void FB_FetchTrialKeys(String expID,String userID) {
        trialKeys.clear();
        DocumentReference docRef = db.collection("Experiments").document(expID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()&&(Boolean)document.getData().get("published")==true) {//combine into one really big conditional?
                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + trialKeys);
                        //FB_FetchNotSubscribed(subscriptionKeys);
                    }
                    else if(document.exists()&&(Boolean)document.getData().get("published")==false&&(String)document.getData().get("ownerID")==userID){
                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    public void FB_FetchTrials(String expID,String userID) {
        trialKeys.clear();
        DocumentReference docRef = db.collection("Experiments").document(expID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()&&(Boolean)document.getData().get("published")==true) {//combine into one really big conditional?
                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + trialKeys);
                        //FB_FetchNotSubscribed(subscriptionKeys);
                    }
                    else if(document.exists()&&(Boolean)document.getData().get("published")==false&&(String)document.getData().get("ownerID")==userID){
                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }

}
