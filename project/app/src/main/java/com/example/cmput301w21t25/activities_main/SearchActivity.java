package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.FilterSearchFragment;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.managers.SearchManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity starts the search activity which filters through a list of unsubscribed, published
 * experiments based on a filter that the user inputs
 */

public class SearchActivity extends AppCompatActivity implements FilterSearchFragment.OnFragmentInteractionListener{
    //
    public SearchManager searchManager = new SearchManager();
    ListView browseList;
    ArrayAdapter<Experiment> experimentArrayAdapter;
    Button searchButton;
    String allKeywords;

    private String userID;


    @Override
    protected void onCreate(Bundle passedData) {
        Log.d("onCreate PASS", "hello!");
        super.onCreate(passedData);
        setContentView(R.layout.activity_browse_not_subbed);

        /*setup the custom toolbar!
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.custom_Blue_dark));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        userID = getIntent().getStringExtra("USER_ID");

        browseList = findViewById(R.id.search_exp_list_view);
        experimentArrayAdapter = new CustomListExperiment(this, experimentList);
        browseList.setAdapter(experimentArrayAdapter);

        browseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DK: ", "Position clicked = " + position);
                Experiment experiment = (Experiment) browseList.getItemAtPosition(position);
                Intent viewExp = new Intent(SearchActivity.this, ViewExperimentActivity.class);

                Bundle expBundle = new Bundle();
                expBundle.putSerializable("EXP_OBJ", experiment);

                viewExp.putExtra("USER_ID", userID);
                viewExp.putExtra("EXP_BUNDLE", expBundle);

                startActivity(viewExp);
            }
        });



        searchButton = findViewById(R.id.exp_filter_button);

        FB_FetchExperimentList(userID);
        //finish();

        searchButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterSearchFragment().show(getSupportFragmentManager(),"FILTER_SEARCH");
            }
        });

    }

    //sets the options menu!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_blue,menu);
        return true;
    }

    /**
     * When the Filter button is pressed, the keywords entered are returned before being passed into
     * the database
     * @param allKeywords the keywords to be returned and passed into the database
     */
    @Override
    public void onOkPressed(String allKeywords) {
        //Toast.makeText(SearchActivity.this,allKeywords,Toast.LENGTH_SHORT).show();
        this.allKeywords = allKeywords;
        Log.d("returning",allKeywords);
        FB_FetchExperimentList(userID);
        //searchManager.searchExperiments(allKeywords, experimentList);
    }

    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> subscriptionKeys = new ArrayList<String>();
    ArrayList<Experiment> experimentList = new ArrayList<Experiment>();

    /**
     * This fetches the list of all experiments
     * @param id id of the user
     */
    public void FB_FetchExperimentList(String id) {
        subscriptionKeys.clear();
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        subscriptionKeys = (ArrayList<String>) document.getData().get("subscriptions");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + subscriptionKeys);
                        FB_FetchNotSubscribed(subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * This fetches a list of all subscribed experiments
     * @param subscriptionKeys the keys of the experiments which are subbed to
     */
    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches experiments that are NOT subscribed AND published
    public void FB_FetchNotSubscribed(ArrayList<String> subscriptionKeys) {
        experimentList.clear();//<------------------------------------------------ARRAY OF EXPERIMENTS THAT ARE FETCHED
            DocumentReference docRef = db.collection("Experiments").document();
            db.collection("Experiments")
                    .whereEqualTo("published", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //ArrayList<Experiment> test2 = new ArrayList<Experiment>();
                                    //test2.add(new BinomealExperiment());
                                    //test2.add(new Experiment());
                                    //Log.d("test",test2.get(1).getName());
                                    if (document.exists() && subscriptionKeys.contains(document.getId()) != true) {
                                        String type = (String)document.getData().get("type");
                                        if(type!=null){
                                            //calls the create trial(parent id, parent type)
                                            switch(type){
                                                case "binomial":
                                                    BinomialExperiment binExp = document.toObject(BinomialExperiment.class);
                                                    binExp.setFb_id(document.getId());
                                                    experimentList.add(binExp);
                                                    Log.d("BINOMIAL", String.valueOf(experimentList));
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    Log.d("BUTTS", "Life is Trash");
                                                    Log.d("YA-DB: ", "SearchResults " + experimentList.get(0).getName());
                                                    break;
                                                case"count":
                                                    final CountExperiment countExp = document.toObject(CountExperiment.class);
                                                    countExp.setFb_id(document.getId());
                                                    experimentList.add(countExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    Log.d("BUTTS", "Life is Trash");
                                                    Log.d("StupidWhy", String.valueOf(countExp.getTags()));
                                                    break;
                                                case "nonnegative count":
                                                    NonNegCountExperiment nnCountExp = document.toObject(NonNegCountExperiment.class);
                                                    nnCountExp.setFb_id(document.getId());
                                                    experimentList.add(nnCountExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    Log.d("BUTTS", "Life is Trash");
                                                    break;
                                                case"measurement":
                                                    MeasurementExperiment mesExp = document.toObject(MeasurementExperiment.class);
                                                    mesExp.setFb_id(document.getId());
                                                    experimentList.add(mesExp);
                                                    experimentArrayAdapter.notifyDataSetChanged();
                                                    Log.d("BUTTS", "Life is Trash");
                                                    //Log.d("StupidWhy", String.valueOf(mesExp.getTags()));
                                                    break;
                                                default:
                                                    Log.d("YA-DB: ","this experiment was not assigned the correct class when it was uploaded so i dont know what class to make");
                                            }
                                        }
                                        //Experiment test = document.toObject(Experiment.class);
                                        //experimentList.add(test);
                                        //inside here update the feilds and stuff
                                    }
                                    else{
                                        Log.d("YA-DB: ", "this is already subbed to");
                                    }
                                }
                                //call search manager here
                                if(allKeywords != null){
                                    //Get keyword matches
                                    ArrayList<Experiment> temp = searchManager.searchExperiments(allKeywords, experimentList);
                                    experimentList.clear();
                                    experimentList.addAll(temp);
                                    experimentArrayAdapter.notifyDataSetChanged();
                                }
                                else{
                                    Log.d("YA-DB returning", "keywords is null");
                                }

                            }
                        }
                    });

    }

    /**
     * Is called when a user clicks on their profile image
     * Will switch to a profile view activity
     * Curtis
     * @param view
     */
    public void viewBrowseiButton(View view) {
        //switch to profileView, pass userId
        Intent intent = new Intent(SearchActivity.this, MyUserProfileActivity.class);
        intent.putExtra("userID", userID);
        intent.putExtra("prevScreen", "Browse");
        startActivity(intent);
    }
}
