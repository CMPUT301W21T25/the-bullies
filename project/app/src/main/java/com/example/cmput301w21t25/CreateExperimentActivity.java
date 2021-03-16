package com.example.cmput301w21t25;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class CreateExperimentActivity extends AppCompatActivity {
//(String name, String ownerID, String description, Location region, ArrayList<String> tags, Boolean geoEnabled, Boolean published, String type, Date date)
    EditText experimentName;
    EditText experimentDescription;
    EditText experimentTags;
    String type;

    Date experimentDate;
    Location experimentLocation;

    CheckBox published;
    CheckBox geolocationEnabled;

    String experimentOwner;

    ExperimentManager experimentManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void onCreate(Bundle passedData){
        super.onCreate(passedData);
        //Change layout
        setContentView(R.layout.activity_create_experiment);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        //User ID for testing (has owned experiment): fdNzWupOTDKvwkrVHMADau

        //Get the user's name from their profile
        DocumentReference docRef = db.collection("UserProfile").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String experimentOwner = (String) document.getData().get("name");
                    }
                }
            }
        });

        //This is temp I don't know what to do for location
        Location testLocal = new Location("edm");

        experimentName = findViewById(R.id.editTextExpName);
        experimentDescription = findViewById(R.id.editTextEnterDescription);
        //experimentTags = findViewById(R.id.)

        experimentManager.FB_CreateExperiment(experimentName, experimentOwner, experimentDescription, testLocal, ArrayList<String> tags, Boolean geoEnabled, Boolean published, String type, Date date)
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonCount:
                if (checked)
                    type = "count";
                    break;
            case R.id.radioButtonNonNegInt:
                if (checked)
                    type = "nonnegative count";
                    break;
            case R.id.radioButtonMeasurement:
                if (checked)
                    type = "measurement";
                    break;
            case R.id.radioButtonBinomial:
                if (checked)
                    type = "binomial";
                    break;
        }
    }

    public ArrayList<String> parseKeywords(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, ",");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        return keywordList;
    }

}
