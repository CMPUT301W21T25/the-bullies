package com.example.cmput301w21t25.activities_experiments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_forum.ForumActivity;
import com.example.cmput301w21t25.activities_main.SearchActivity;
import com.example.cmput301w21t25.activities_trials.AddTrialActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.activities_user.OtherUserProfileActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Allows for extracting the created experiments from the database
 */
public class ViewCreatedExperimentActivity extends AppCompatActivity {

    private String expID;
    private String ownerID;
    private String userID;
    private Bundle expBundle;
    private ExperimentManager experimentManager = new ExperimentManager();
    private TrialManager trialManager = new TrialManager();
    private Experiment exp;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_created_experiment);

        final Button editButton = findViewById(R.id.edit_button);
        final Button publishButton = findViewById(R.id.publish_button);
        final Button addTrialButton = findViewById(R.id.add_trial_button);
        final Button commentsButton = findViewById(R.id.comments_button);

        userID = getIntent().getStringExtra("USER_ID");
        exp = unpackExperiment();
        expID = exp.getFb_id(); //ck


        TextView expName = findViewById(R.id.exp_name_text_view);
        TextView expDesc = findViewById(R.id.exp_description_text_view);
        TextView expType = findViewById(R.id.exp_type_text_view);
        TextView minTrials = findViewById(R.id.min_trials_text_view);
        TextView currTrials = findViewById(R.id.current_trials_text_view);

//        expName.setText(exp.getName());
//        expDesc.setText(exp.getDescription());
//        expType.setText(exp.getType());
//        minTrials.setText("Minimum Trials: " + String.valueOf(exp.getMinNumTrials()));
//        currTrials.setText("Current Trials: " + String.valueOf(exp.getCurrentNumTrials()));
        experimentManager.FB_UpdateExperimentTextViews(expID,expName,expDesc,expType,minTrials);
        trialManager.FB_FetchPublishedTrialCount(exp,currTrials);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentManager.FB_UpdatePublished(true, expID);
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTrial = new Intent(ViewCreatedExperimentActivity.this, AddTrialActivity.class);
                newTrial.putExtra("USER_ID", userID);
                newTrial.putExtra("TRIAL_PARENT", exp);
                startActivity(newTrial);
            }
        });

        commentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewComments = new Intent(ViewCreatedExperimentActivity.this, ForumActivity.class);
                viewComments.putExtra("USER_ID", userID);
                viewComments.putExtra("FORUM_EXPERIMENT", exp);
                startActivity(viewComments);
            }
        });

    }

    private Experiment unpackExperiment() {

        expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP_OBJ");
        return exp;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    /**
     * Allows for viewing of the owner of the experiment's profile
     * @param id id of the owner
     */
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
                                            Intent intent = new Intent(ViewCreatedExperimentActivity.this, MyUserProfileActivity.class);
                                            intent.putExtra("userID", userID);
                                            intent.putExtra("prevScreen", "Experiment");
                                            intent.putExtra("EXP_BUNDLE", expBundle);
                                            startActivity(intent);
                                        }
                                        else {
                                            //switch to otherprofile
                                            Intent intent = new Intent(ViewCreatedExperimentActivity.this, OtherUserProfileActivity.class);
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

        Intent intent = new Intent(ViewCreatedExperimentActivity.this, SearchActivity.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);

    }

    public void dataButton(View view) {
        Intent switchScreens = new Intent(ViewCreatedExperimentActivity.this, ExperimentDataActivity.class);
        switchScreens.putExtra("USER_ID", userID);
        switchScreens.putExtra("EXP", exp);
        startActivity(switchScreens);
    }

}
