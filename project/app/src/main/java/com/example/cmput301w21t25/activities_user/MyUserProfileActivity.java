package com.example.cmput301w21t25.activities_user;

import android.content.Intent;
import android.os.Bundle;
//import android.os.UserManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewExperimentActivity;
import com.example.cmput301w21t25.activities_main.HomeOwnedActivity;
import com.example.cmput301w21t25.activities_main.HomeSubbedActivity;
import com.example.cmput301w21t25.activities_main.SearchActivity;
import com.example.cmput301w21t25.activities_trials.AddTrialActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.cmput301w21t25.managers.UserManager;


public class MyUserProfileActivity extends AppCompatActivity {
    //attributes
    private String Username;
    private String ContactInfo;
    private String userID;
    private String prevScreen;
    private Bundle expBundle;
    private Experiment exp;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_myprofile_view);
        userID = getIntent().getStringExtra("userID");
        prevScreen = getIntent().getStringExtra("prevScreen");
        //try to get bundle, (only from experiment view)
        expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");

        FB_FetchUserInfo(userID);
    }




    /********************************************
     * DB Functions HERE!!!!!!!!!!!!!!!!!!!!!!!!!
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //so far this is the only information that comprises the user profile th
    public void FB_FetchUserInfo(String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Username = (String)document.getData().get("name");
                        ContactInfo = (String)document.getData().get("email");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + Username);
                        //update EDITTEXT fields
                        EditText editName = findViewById(R.id.updateName);
                        EditText editEmail = findViewById(R.id.updateEmail);
                        editName.setText(Username);
                        editEmail.setText(ContactInfo);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }

    public void goBackButton(View view) {
        Intent intent = null;
        switch (prevScreen) {
            case "Owned":
                intent = new Intent(MyUserProfileActivity.this, HomeOwnedActivity.class);
                Log.i("curtis", "going back to owned");
                break;
            case "Subbed":
                intent = new Intent(MyUserProfileActivity.this, HomeSubbedActivity.class);
                Log.i("curtis", "going back to subbed");
                break;
            case "Experiment":
                // go back to experiment view
                intent = new Intent(MyUserProfileActivity.this, ViewExperimentActivity.class);
                intent.putExtra("EXP_BUNDLE", expBundle);
                Log.i("curtis", "going back to viewing an experiment");
                break;
            case "Browse":
                intent = new Intent(MyUserProfileActivity.this, SearchActivity.class);
                Log.i("curtis", "going back to browse page");
                break;
            case "AddTrial":
                intent = new Intent(MyUserProfileActivity.this, AddTrialActivity.class);
                //add experiment
                intent.putExtra("TRIAL_PARENT", exp);
                Log.i("curtis", "going back to add trial page");
                break;

            default:
                intent = new Intent(MyUserProfileActivity.this, HomeOwnedActivity.class);
                Log.i("curtis", "going back to default owned");
        }

        intent.putExtra("USER_ID", userID);
        startActivity(intent);
    }
    public void updateButton(View view) {
        EditText newName = findViewById(R.id.updateName);
        EditText newEmail = findViewById(R.id.updateEmail);
        String name = newName.getText().toString();
        String email = newEmail.getText().toString();
        UserManager userManager = new UserManager();
        userManager.FB_UpdateName(name, userID);
        userManager.FB_UpdateEmail(email, userID);

        ExperimentManager experimentManager = new ExperimentManager();
        experimentManager.FB_UpdateName(userID);

        goBackButton(view);
    }
}
