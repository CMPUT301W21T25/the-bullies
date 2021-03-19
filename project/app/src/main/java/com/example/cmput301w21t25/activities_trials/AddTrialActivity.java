package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.custom.CustomListTrial;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.NonNegCountTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddTrialActivity extends AppCompatActivity {

    ListView trialListView;
    ArrayAdapter<Trial> trialArrayAdapter;
    FloatingActionButton addTrialButton;

    String userID;
    Experiment exp;
    String expID;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_trial_list);

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        expID = exp.getFb_id();
        FB_FetchTrialKeys(expID,userID,exp);

        addTrialButton = findViewById(R.id.trial_create_button);
        trialListView = findViewById(R.id.add_trial_list);
        trialArrayAdapter = new CustomListTrial(this, trialList);
        trialListView.setAdapter(trialArrayAdapter);

        addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches a conduct trial activity based on type of experiment
                Intent switchScreen = null;
                switch (exp.getType()) {
                    case "count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "nonnegative count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductNonnegativeCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "binomial":
                        switchScreen = new Intent(AddTrialActivity.this, ConductBinomialTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "measurement":
                        switchScreen = new Intent(AddTrialActivity.this, ConductMeasurementTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                }
            }
        });

        //finish();
    }


    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> trialKeys = new ArrayList<String>();
    ArrayList<Trial> trialList = new ArrayList<Trial>();
    public void FB_FetchTrialKeys(String expID,String userID,Experiment parent) {
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
                        FB_FetchTrials(parent);
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
    public void FB_FetchTrials(Experiment parent) {
        trialList.clear();
        String type = parent.getType();
        if(!trialKeys.isEmpty()){
            for (String key : trialKeys) {
                DocumentReference docRef = db.collection("TrialDocs").document(key);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("YA-DB: ", "testing");
                                switch (type) {
                                    case "binomial":
                                        //ArrayList<Experiment>test = new ArrayList<Experiment>();
                                        BinomialTrial binTrial = document.toObject(BinomialTrial.class);
                                        trialList.add(binTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        break;
                                    case "count":
                                        CountTrial countTrial = document.toObject(CountTrial.class);
                                        trialList.add(countTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        Log.d("YA-DB: ", String.valueOf(trialList));
                                        break;
                                    case "nonnegative count":
                                        NonNegCountTrial nnCountTrial = document.toObject(NonNegCountTrial.class);
                                        trialList.add(nnCountTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        break;
                                    case "measurement":
                                        MeasurementTrial mesTrial = document.toObject(MeasurementTrial.class);
                                        trialList.add(mesTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        break;
                                    default:
                                        Log.d("YA-DB: ", "this experiment was not assigned the correct class when it was uploaded so i dont know what class to make");
                                }
                            }
                        } else {
                            Log.d("YA-DB: ", "search failed ");
                        }
                    }
                });
            }
        }
    }

}
