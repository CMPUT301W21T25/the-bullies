package com.example.cmput301w21t25.activities_user;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewSearchedExperimentActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity is used to view the profile of user other then the owner of the device
 * @author Curtis
 */
public class OtherUserProfileActivity extends AppCompatActivity {
    //attributes
    private String Username;
    private String ContactInfo;
    private String ownerID;
    private String prevScreen;
    private Bundle expBundle;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_otherprofile_view);
        ownerID = getIntent().getStringExtra("ownerID");
        prevScreen = getIntent().getStringExtra("prevScreen");
        //try to get bundle, (only from experiment view)
        expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Button backButton = findViewById(R.id.button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });

        FB_FetchUserInfo(ownerID);
    }


    /**
     * This method fetches user info from the database using the provided id
     * @param id the id of the user
     */
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
                        //update VIEWTEXT fields
                        TextView viewName = findViewById(R.id.viewName);
                        viewName.setText(Username);
                        TextView viewEmail = findViewById(R.id.viewEmail);
                        viewEmail.setText(ContactInfo);



                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }


    /**
     * Go back to viewing the experiment
     */
    public void goBack() {
        Intent intent = null;
        switch (prevScreen) {
            case "Experiment":
                intent = new Intent(OtherUserProfileActivity.this, ViewSearchedExperimentActivity.class);
                intent.putExtra("EXP_BUNDLE", expBundle);
                break;

        }

        startActivity(intent);
    }
}





