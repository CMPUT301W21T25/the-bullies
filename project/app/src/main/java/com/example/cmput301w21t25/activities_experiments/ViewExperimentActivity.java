package com.example.cmput301w21t25.activities_experiments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.activities_user.OtherUserProfileActivity;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewExperimentActivity extends AppCompatActivity {

    private String expID;
    private String ownerID;
    private String userID;
    private Bundle expBundle;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_experiment);

        userID = getIntent().getStringExtra("USER_ID");
        Experiment exp = unpackExperiment();

        TextView expName = findViewById(R.id.exp_name_text_view);
        TextView expDesc = findViewById(R.id.exp_description_text_view);

        expName.setText(exp.getName());
        expDesc.setText(exp.getDescription()); // currently an error but should resolve on merge
        expID = exp.getFb_id(); //ck

    }

    private Experiment unpackExperiment() {

        expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP_OBJ");
        return exp;
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
                            case "binomial":
                                BinomialExperiment binomialExperiment = document.toObject(BinomialExperiment.class);
                                binomialExperiment.setFb_id(document.getId());
                                break;
                            case"count":
                                CountExperiment countExperiment = document.toObject(CountExperiment.class);
                                countExperiment.setFb_id(document.getId());
                                break;
                            case"non-neg-count":
                                NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                nnCountExp.setFb_id(document.getId());
                                break;
                            case"measurement":
                                MeasurementExperiment measurementExperiment = document.toObject(MeasurementExperiment.class);
                                measurementExperiment.setFb_id(document.getId());
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
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownerID = (String)document.getData().get("ownerID");
                        DocumentReference docRef = db.collection("UserProfile").document(ownerID);
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

                                        //check if current user = experiment owner
                                        if (ownerID.equals(userID)) {
                                            //switch to myprofile
                                            Intent intent = new Intent(ViewExperimentActivity.this, MyUserProfileActivity.class);
                                            intent.putExtra("userID", userID);
                                            intent.putExtra("prevScreen", "Experiment");
                                            intent.putExtra("EXP_BUNDLE", expBundle);
                                            startActivity(intent);
                                        }
                                        else {
                                            //switch to otherprofile
                                            Intent intent = new Intent(ViewExperimentActivity.this, OtherUserProfileActivity.class);
                                            intent.putExtra("ownerID", ownerID);
                                            intent.putExtra("prevScreen", "Experiment");
                                            intent.putExtra("EXP_BUNDLE", expBundle);
                                            startActivity(intent);
                                        }

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
        FB_FetchOwnerProfile(expID);
    }

    public void subscribeButton(View view) {
        //This method will subscribe the user to the experiment
        //do i need to check if we're already subscribed? (firestore wont add duplicates)
        DocumentReference docRef = db.collection("UserProfile").document(userID);
        docRef
                .update("subscriptions", FieldValue.arrayUnion(expID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("curtis", "you subscribed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("curtis", "failed to subscribe");
                    }
                });
    }

}
