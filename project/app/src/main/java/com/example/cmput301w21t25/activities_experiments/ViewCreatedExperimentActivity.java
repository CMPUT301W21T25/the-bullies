package com.example.cmput301w21t25.activities_experiments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_forum.ForumActivity;
import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.activities_main.SearchExperimentsActivity;
import com.example.cmput301w21t25.activities_trials.AddTrialActivity;
import com.example.cmput301w21t25.activities_trials.HideTrialActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.activities_user.OtherUserProfileActivity;
import com.example.cmput301w21t25.customDialogs.EndExperimentDialogFragment;
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
public class ViewCreatedExperimentActivity extends AppCompatActivity implements EndExperimentDialogFragment.OnFragmentInteractionListenerEnd {

    private String expID;
    private String ownerID;
    private String userID;
    private Bundle expBundle;
    private ExperimentManager experimentManager = new ExperimentManager();
    private TrialManager trialManager = new TrialManager();
    private Experiment exp;

    Button addTrialButton;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_created_experiment);

        userID = getIntent().getStringExtra("USER_ID");
        exp = unpackExperiment();
        expID = exp.getFb_id(); //ck

        addTrialButton = findViewById(R.id.add_trial_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button endButton = findViewById(R.id.end_button);
        final Button publishButton = findViewById(R.id.publish_button);
        final Button unpublishButton = findViewById(R.id.unpublish_button);
        final Button commentsButton = findViewById(R.id.comments_button);
        final Button dataButton = findViewById(R.id.view_data_button);
        final Button hideTrialButton = findViewById(R.id.hideTrialsButton);

        if (exp.isPublished()) {
            publishButton.setVisibility(View.GONE);
            unpublishButton.setVisibility(View.VISIBLE);
        }
        else {
            unpublishButton.setVisibility(View.GONE);
            publishButton.setVisibility(View.VISIBLE);
        }
        if (exp.getIsEnded()) {
            addTrialButton.setBackgroundColor(getResources().getColor(R.color.custom_Grey_translucent));
        }
        else {
            addTrialButton.setBackgroundColor(getResources().getColor(R.color.custom_Yellow_light));
        }


        TextView expName = findViewById(R.id.exp_name_text_view);
        TextView expDesc = findViewById(R.id.exp_description_text_view);
        TextView expType = findViewById(R.id.exp_type_text_view);
        TextView minTrials = findViewById(R.id.min_trials_text_view);
        TextView currTrials = findViewById(R.id.current_trials_text_view);
        TextView region = findViewById(R.id.region_text_view);

//        expName.setText(exp.getName());
//        expDesc.setText(exp.getDescription());
//        expType.setText(exp.getType());
//        minTrials.setText("Minimum Trials: " + String.valueOf(exp.getMinNumTrials()));
//        currTrials.setText("Current Trials: " + String.valueOf(exp.getCurrentNumTrials()));
        experimentManager.FB_UpdateExperimentTextViews(expID,expName,expDesc,expType,minTrials,region);
        trialManager.FB_FetchPublishedTrialCount(exp,currTrials);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = currTrials.getText().toString();
                if (exp.getMinNumTrials() <= Integer.parseInt(temp.substring(temp.length() - 1)) && !exp.getIsEnded()) {
                    new EndExperimentDialogFragment().show(getSupportFragmentManager(), "END_EXPERIMENT");
                }
                else if (!exp.getIsEnded()){
                    Toast.makeText(ViewCreatedExperimentActivity.this, "Must have minimum number of trials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentManager.FB_UpdatePublished(true, expID);
                publishButton.setVisibility(View.GONE);
                unpublishButton.setVisibility(View.VISIBLE);
            }
        });

        unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experimentManager.FB_UpdatePublished(false, expID);
                unpublishButton.setVisibility(View.GONE);
                publishButton.setVisibility(View.VISIBLE);
            }
        });

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exp.getIsEnded()) {
                    Intent newTrial = new Intent(ViewCreatedExperimentActivity.this, AddTrialActivity.class);

                    //This line makes sure that this activity is not saved in the history stack
                    newTrial.addFlags(newTrial.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

                    newTrial.putExtra("USER_ID", userID);
                    newTrial.putExtra("TRIAL_PARENT", exp);
                    startActivity(newTrial);
                }
                else {
                    Toast.makeText(ViewCreatedExperimentActivity.this, "This experiment has been ended.", Toast.LENGTH_SHORT).show();
                }
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

        dataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent switchScreens = new Intent(ViewCreatedExperimentActivity.this, ExperimentDataActivity.class);
                switchScreens.putExtra("USER_ID", userID);
                switchScreens.putExtra("EXP", exp);
                startActivity(switchScreens);
            }
        });

        hideTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hideTrials = new Intent(ViewCreatedExperimentActivity.this, HideTrialActivity.class);
                hideTrials.putExtra("USER_ID", userID);
                hideTrials.putExtra("EXPERIMENT", exp);
                startActivity(hideTrials);
            }
        });

    }

    /**
     * This event is menu setup!
     * @param menu this is the menu being integrated
     * @return true to indicate there is a menu (return false to turn off)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     * This event is for menu item setup
     * @param item these are items that will be added to the menu
     * @return @return true to indicate there is this item (return false to turn off)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(ViewCreatedExperimentActivity.this, CreatedExperimentsActivity.class);

                home.putExtra("USER_ID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(ViewCreatedExperimentActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("USER_ID", userID);
                user_settings.putExtra("prevScreen", "Owned");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        experimentManager.FB_UpdateContributorUserKeys(exp.getContributorUsersKeys(),expID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("curtis", "failed to subscribe");
                    }
                });

        Intent intent = new Intent(ViewCreatedExperimentActivity.this, SearchExperimentsActivity.class);
        intent.putExtra("USER_ID", userID);
        startActivity(intent);

    }

    @Override
    public void endExperiment() {
        experimentManager.FB_UpdateEnded(true, expID);
        addTrialButton.setBackgroundColor(getResources().getColor(R.color.custom_Grey_translucent));
        exp.setIsEnded(true);
    }
}
