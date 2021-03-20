package com.example.cmput301w21t25.activities_trials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.HomeSubbedActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListTrial;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.NonNegCountTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * this activity is used to add new trials and view unpublished trials
 */
public class AddTrialActivity extends AppCompatActivity {

    ListView trialListView;
    ArrayAdapter<Trial> trialArrayAdapter;
    FloatingActionButton addTrialButton;

    private String userID;
    private Experiment exp;
    private String expID;

    private TrialManager trialManager;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_trial_list);

        trialManager = new TrialManager();

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        expID = exp.getFb_id();
        FB_FetchTrialKeys(expID,userID,exp);

        addTrialButton = findViewById(R.id.trial_create_button);
        trialListView = findViewById(R.id.add_trial_list);
        trialArrayAdapter = new CustomListTrial(this, trialList);
        trialListView.setAdapter(trialArrayAdapter);

        trialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                publishTrial(position);
            }
        });

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


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> trialKeys = new ArrayList<String>();
    ArrayList<Trial> trialList = new ArrayList<Trial>();

    /**
     * this method is used to fetch Trials using the array of keys stored in the parent experiment's
     * document and calling FB_FetchTrials on them
     * @param expID id of parent experiment
     * @param userID id of parent user
     * @param parent parent experiment object
     */
    public void FB_FetchTrialKeys(String expID,String userID,Experiment parent) {
        trialKeys.clear();
        CollectionReference collectionReference = db.collection("Experiments");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                trialKeys.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    if(expID.equals(doc.getId())){
                        trialKeys = (ArrayList<String>) doc.getData().get("trialKeys");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + trialKeys);
                        FB_FetchTrials(parent);
                    }
//                    Log.d(TAG, String.valueOf(doc.getData().get("Province Name")));
//                    String city = doc.getId();
//                    String province = (String) doc.getData().get("Province Name");
//                    cityDataList.add(new City(city, province)); // Adding the cities and provinces from FireStore
                }
            }
        });


//        DocumentReference docRef = db.collection("Experiments").document(expID);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//
//                    if (document.exists()) {//combine into one really big conditional?
//
//                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
//                        Log.d("YA-DB: ", "DocumentSnapshot data: " + trialKeys);
//                        FB_FetchTrials(parent);
//                    }
//                } else {
//                    Log.d("YA-DB: ", "get failed with ", task.getException());
//                }
//            }
//        });
    }

    /**
     * this method fetches the trials from the fetched keys and parent experiment
     * @param parent parent experiment object
     */
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
                            if (document.exists()&&(Boolean)document.getData().get("published")==false) {
                                Log.d("YA-DB: ", "testing");
                                switch (type) {
                                    case "binomial":
                                        //ArrayList<Experiment>test = new ArrayList<Experiment>();
                                        BinomialTrial binTrial = document.toObject(BinomialTrial.class);
                                        binTrial.setTrialId(document.getId());
                                        trialList.add(binTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        break;
                                    case "count":
                                        CountTrial countTrial = document.toObject(CountTrial.class);
                                        countTrial.setTrialId(document.getId());
                                        trialList.add(countTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        Log.d("YA-DB: ", String.valueOf(trialList));
                                        break;
                                    case "nonnegative count":
                                        NonNegCountTrial nnCountTrial = document.toObject(NonNegCountTrial.class);
                                        nnCountTrial.setTrialId(document.getId());
                                        trialList.add(nnCountTrial);
                                        trialArrayAdapter.notifyDataSetChanged();
                                        break;
                                    case "measurement":
                                        MeasurementTrial mesTrial = document.toObject(MeasurementTrial.class);
                                        mesTrial.setTrialId(document.getId());
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

    /**
     * this method defines the behaviour of the addTrialButton
     * @param view
     */
    public void addTrialiButton(View view) {
        //switch to profileView, pass userId
        Intent intent = new Intent(AddTrialActivity.this, MyUserProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("prevScreen", "AddTrial");
        //bundle experiment to return to

        intent.putExtra("TRIAL_PARENT", exp);
        startActivity(intent);

    }

    /**
     * Published a trial at the position clicked on in the trial list view
     * @param position
     * The index of the trial you want to publish in the list
     */
    public void publishTrial(int position) {
        Trial temp = trialList.remove(position);
        temp.setPublished(true);
        trialManager.FB_UpdatePublished(true, temp.getTrialId());
        trialArrayAdapter.notifyDataSetChanged();
    }

}
