package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.FilterSearchFragment;
import com.example.cmput301w21t25.FirestoreExperimentCallback;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
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
    private ArrayAdapter<Experiment> experimentArrayAdapter;
    private ArrayList<Experiment> experimentList = new ArrayList<Experiment>();
    Button searchButton;
    String allKeywords;
    private ExperimentManager experimentManager = new ExperimentManager();

    private String userID;


    @Override
    protected void onCreate(Bundle passedData) {
        Log.d("onCreate PASS", "hello!");
        super.onCreate(passedData);
        setContentView(R.layout.activity_browse_not_subbed);

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

        experimentManager.FB_UpdateBrowseExperimentAdapter(userID, experimentArrayAdapter, experimentList, new FirestoreExperimentCallback() {
            @Override
            public void onCallback(ArrayList<Experiment> list) {

            }
        });
        //finish();

        searchButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterSearchFragment().show(getSupportFragmentManager(),"FILTER_SEARCH");
            }
        });

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
        experimentManager.FB_UpdateBrowseExperimentAdapter(userID, experimentArrayAdapter, experimentList, new FirestoreExperimentCallback() {
            @Override
            public void onCallback(ArrayList<Experiment> list) {
                ArrayList<Experiment> temp =  searchManager.searchExperiments(allKeywords, list);
                experimentList.clear();
                experimentList.addAll(temp);
                experimentArrayAdapter.notifyDataSetChanged();
            }
        });
        //searchManager.searchExperiments(allKeywords, experimentList);
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
