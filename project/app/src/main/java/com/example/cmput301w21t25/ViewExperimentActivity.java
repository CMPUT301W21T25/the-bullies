package com.example.cmput301w21t25;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewExperimentActivity extends AppCompatActivity {

    private String expID;
    private String ownerID;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String experimentID;
        experimentID = getIntent().getStringExtra("EXP_ID");
        expID = experimentID;

        currentUserID = getIntent().getStringExtra("");

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
                        ownerID = userID;
                        DocumentReference docRef = db.collection("UserProfile").document(userID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("YA-DB:", "User document retrieved passing ID to MyUserProfileActivity");
                                        //EDEN:
                                        //For list testing I'm going to send it to homeOwned instead
                                        //Can return to userProfile activity later
                                        Intent intent = new Intent(getBaseContext(), MyUserProfileActivity.class);
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

    /**
     * Is called when a user clicks on the owners profile image while viewing an experiment
     * Will switch to a profile view activity (either myuser or otheruser)
     * Curtis
     * @param view
     */
    public void viewExpOwnerButton(View view) {
        //first we need to get the experiment owner
        FB_FetchOwnerProfile(expID); //this updates the class attribute ownerID

        //check if current user = experiment owner
        if (ownerID == currentUserID) {
            //switch to myprofile, pass myID
            Intent intent = new Intent(ViewExperimentActivity.this, MyUserProfileActivity.class);
            intent.putExtra("userID", currentUserID);
            startActivity(intent);
        }
        else {
            //switch to otherprofile, pass expOwnerID
            Intent intent = new Intent(ViewExperimentActivity.this, OtherUserProfileActivity.class);
            intent.putExtra("userID", ownerID);
            startActivity(intent);
        }

    }

}
