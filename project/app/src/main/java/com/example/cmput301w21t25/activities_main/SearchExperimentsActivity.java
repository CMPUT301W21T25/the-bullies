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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.FilterSearchFragment;
import com.example.cmput301w21t25.FirestoreExperimentCallback;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.managers.SearchManager;

import java.util.ArrayList;

/**
 * This activity starts the search activity which filters through a list of unsubscribed, published
 * experiments based on a filter that the user inputs
 */

public class SearchExperimentsActivity extends AppCompatActivity implements FilterSearchFragment.OnFragmentInteractionListener{
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
                Intent viewExp = new Intent(SearchExperimentsActivity.this, ViewExperimentActivity.class);

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

    //Toolbar Menu setup!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_blue,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(SearchExperimentsActivity.this, CreatedExperimentsActivity.class);
                home.putExtra("userID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(SearchExperimentsActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("userID", userID);
                user_settings.putExtra("prevScreen", "Browse");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
//    public void viewBrowseiButton(View view) {
//        //switch to profileView, pass userId
//        Intent intent = new Intent(SearchActivity.this, MyUserProfileActivity.class);
//        intent.putExtra("userID", userID);
//        intent.putExtra("prevScreen", "Browse");
//        startActivity(intent);
//    }
}
