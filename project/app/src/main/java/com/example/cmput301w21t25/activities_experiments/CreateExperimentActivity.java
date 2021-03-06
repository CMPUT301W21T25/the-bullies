package com.example.cmput301w21t25.activities_experiments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * This activity is used to create a new experiment.
 * It contains all the necessary fields, buttons, and input validation.
 */
public class CreateExperimentActivity extends AppCompatActivity {
    //(String name, String ownerID, String description, Location region, ArrayList<String> tags, Boolean geoEnabled, Boolean published, String type, Date date)
    EditText experimentName;
    EditText experimentDescription;
    EditText experimentTags;
    EditText minimumTrials;
    EditText region;

    ArrayList<String> experimentKeywords;
    String type;
    String experimentOwner;

    CheckBox subscribe;
    CheckBox published;
    CheckBox geolocationEnabled;

    ExperimentManager experimentManager;

    Button createExperiment;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void onCreate(Bundle passedData){
        super.onCreate(passedData);
        setContentView(R.layout.activity_create_experiment);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");

        //Initialize experiment manager
        experimentManager = new ExperimentManager();

        experimentName = findViewById(R.id.editTextExpName);
        experimentDescription = findViewById(R.id.editTextEnterDescription);
        experimentTags = findViewById(R.id.editTextKeywords);
        minimumTrials = findViewById(R.id.editTextMinTrials);
        region = findViewById(R.id.editTextRegion);

        subscribe = findViewById(R.id.checkBoxSubscribe);
        published = findViewById(R.id.checkBoxPublish);
        geolocationEnabled = findViewById(R.id.checkBoxGeolocation);

        createExperiment = findViewById(R.id.buttonCreateExperiment);
        createExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                ArrayList<String> testList = new ArrayList<>();
                testList.add("this");
                testList.add("is");
                testList.add("not a");
                testList.add("test");
                 */
                //experimentManager.FB_CreateExperiment("NEWTestName","fdNzWupOTDKvwkrVHMADau", "this is a test",testLocal,testList,false,false,"abstract",new Date());

                String description = experimentDescription.getText().toString();
                String name = experimentName.getText().toString();

                Toast noInput = Toast.makeText(getApplicationContext(), "Enter required fields", Toast.LENGTH_LONG);
                if(minimumTrials.getText().length()>0){
                    Integer minTrials = Integer.parseInt(minimumTrials.getText().toString());
                    String Region = region.getText().toString();

                    //Experiment keywords parsed and cast to lower case on creation to ensure
                    //compatibility with User keyword search later on
                    experimentKeywords = new ArrayList<String>();
                    String keywords = experimentTags.getText().toString();
                    experimentKeywords = parseKeywords(keywords);


                    Log.d("description", description);
                    Log.d("name", name);
                    Log.d("keywords", experimentKeywords.toString());
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter required fields", Toast.LENGTH_LONG);
                    if(!checkEnterValid(description,name,experimentKeywords) || type==null){
                        toast.show();
                    }else {
                        //Get the user's name from their profile
                        DocumentReference docRef = db.collection("UserProfile").document(userID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        experimentOwner = (String) document.getData().get("name");
                                        experimentManager.FB_CreateExperiment(userID, name, experimentOwner, description, Region, experimentKeywords, subscribe.isChecked(), geolocationEnabled.isChecked(), published.isChecked(), type, new Date(), minTrials);
                                        //
                                    }
                                }
                            }
                        });
                        Intent switchScreen = new Intent(CreateExperimentActivity.this, CreatedExperimentsActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        startActivity(switchScreen);
                    }
                } else{ noInput.show(); }

            }
        });

    }

    //Called on click from the layout
    //leaving this out of onCreate, otherwise it would require 4 buttons
    /**
     * Assigns the experiment a type based on which radio button is selected
     * @param view
     */
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

    /**
     * This method will take a String of keywords and change it into a list of keywards
     * which will be added to the experiment
     * @param keywords String of keywords which are added to the experiment
     * @return returns an ArrayList of keywords
     */
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

    /**
     * This method check if user entered valid input
     * The result will return to decide if the experiment can be create
     * @param description
     * @param name
     * @param keywords
     * @return
     */
    public boolean checkEnterValid(String description, String name, ArrayList<String> keywords){
        boolean checkIfValid=true;
        if(description==null||description.length()==0){
            checkIfValid = false;
        }
        if(name==null||name.length()==0){
            checkIfValid = false;
        }
        if(keywords.isEmpty()){
            checkIfValid = false;
        }
        return checkIfValid;
    }


}
