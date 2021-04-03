package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.CustomToolbar;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.CreateExperimentActivity;
import com.example.cmput301w21t25.activities_experiments.ViewCreatedExperimentActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * this activity shows a list of all the experiments this user has created
 */
public class HomeOwnedActivity extends AppCompatActivity {

    private ListView ownedExperimentsListView;
    private ArrayList<Experiment> ownedExperimentsList;
    private ArrayAdapter<Experiment> experimentAdapter;
    private ExperimentManager experimentManager = new ExperimentManager();
    private String userID;

    //Variables to access on touch events
    private float x1;
    private float x2;
    private float y1;
    private float y2;
    private int publishedTrials = 0;
    private ArrayList<String>key = new ArrayList<String>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_owned);


        /*setup the custom toolbar!
        */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = getIntent().getStringExtra("USER_ID");

        ownedExperimentsList = new ArrayList<Experiment>();
        ownedExperimentsListView = findViewById(R.id.owned_experiment_list);
        experimentAdapter = new CustomListExperiment(this, ownedExperimentsList);
        ownedExperimentsListView.setAdapter(experimentAdapter);

        /////////////////////////////////////////////
        experimentManager.FB_UpdateOwnedExperimentAdapter(userID,experimentAdapter,ownedExperimentsList);





        final FloatingActionButton createExperimentButton = findViewById(R.id.exp_create_button);

        //Prevent ListView from eating onTouchEvent
        ownedExperimentsListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });

        ownedExperimentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("DK: ", "Position clicked = " + position);
                Experiment experiment = (Experiment) ownedExperimentsListView.getItemAtPosition(position);
                //FB_FetchPublishedTrials(experiment);

                int pubTrials = publishedTrials;
                experiment.setCurrentNumTrials(pubTrials);
                Log.d("pubTrials", String.valueOf(pubTrials));
                Log.d("getTrials", String.valueOf(experiment.getCurrentNumTrials()));



                Intent viewExp = new Intent(HomeOwnedActivity.this, ViewCreatedExperimentActivity.class);

                Bundle expBundle = new Bundle();
                expBundle.putSerializable("EXP_OBJ", experiment);

                viewExp.putExtra("USER_ID", userID);
                viewExp.putExtra("EXP_BUNDLE", expBundle);

                startActivity(viewExp);
            }
        });

        // OnClickListener to transfer user to Create Experiment Activity
        createExperimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newExp = new Intent(HomeOwnedActivity.this, CreateExperimentActivity.class);
                newExp.putExtra("USER_ID", userID);
                startActivity(newExp);
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
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(HomeOwnedActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("userID", userID);
                user_settings.putExtra("prevScreen", "Owned");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Screen switching
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float y = (y1 - y2);
                float x = (x1 - x2);

                //To deal with sensitivity so scrolling doesn't switch screens
                if (Math.abs(y) > Math.abs(x)) {
                    return false;
                }

                if (x1 > (x2)) {
                    Intent switchScreen = new Intent(HomeOwnedActivity.this, HomeSubbedActivity.class);
                    switchScreen.putExtra("USER_ID", userID);
                    startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

}
