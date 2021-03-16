package com.example.cmput301w21t25;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InspectExperimentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String experimentID;
        experimentID = getIntent().getStringExtra("EXP_ID");
        FB_FetchExperiment(experimentID);
        finish();
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void FB_FetchExperiment(String id){
        DocumentReference docRef = db.collection("Experiment").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String type = (String)document.getData().get("type");
                        switch (type){
                            case "Binomial":
                                BinomialExperiment binomialExperiment = new BinomialExperiment();
                                binomialExperiment = document.toObject(BinomialExperiment.class);
                                break;
                            case"Count":
                                CountExperiment countExperiment = new CountExperiment();
                                countExperiment = document.toObject(CountExperiment.class);
                                break;
                            case"Measurement":
                                MeasurementExperiment measurementExperiment = new MeasurementExperiment();
                                measurementExperiment = document.toObject(MeasurementExperiment.class);
                                break;
                        }
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    public void FB_FetchOwnerProfile(String id){//the input param is the exp ID
        DocumentReference docRef = db.collection("Experiment").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userID = (String)document.getData().get("owner");
                        DocumentReference docRef = db.collection("UserProfile").document(userID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("YA-DB:", "User document retrieved passing ID to UserProfileActivity");
                                        //EDEN:
                                        //For list testing I'm going to send it to homeOwned instead
                                        //Can return to userProfile activity later
                                        Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
                                        intent.putExtra("USER_ID", userID);
                                        startActivity(intent);
                                    }
                                } else {
                                    Log.d("YA-DB:", "User Profile Query Failed", task.getException());
                                    //this means it couldnt complete query
                                }
                            }
                        });
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
}
