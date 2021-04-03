package com.example.cmput301w21t25.activities_trials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.location.Maps;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * @author Eden
 * this activity is used to conduct trials for count type experiments On completion, sends
 * trial to database as doc, returns to add trial list view.
 */
public class ConductCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    FloatingActionButton incrementButton;
    TextView countDisplay;
    TextView description;

    private Experiment trialParent;
    private String userID;
    private int count = 0;

    private TrialManager trialManager;

    //Location
    private FusedLocationProviderClient locationClient;
    private Location location;
    private Maps maps;
    //End location

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        //Need to pass experiment ID to access title, description, etc. to pass to toolbar
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        trialManager = new TrialManager();

        trialHeader = findViewById(R.id.countExperimentInfo);

        submitTrialButton = findViewById(R.id.submit_trial_count_button);
        incrementButton = findViewById(R.id.increment_count_button);
        countDisplay = findViewById(R.id.trial_count);
        description = findViewById(R.id.countExpDescription);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        //On click, increment the value stored in count, displayed in countDisplay
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCount();
            }
        });

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!trialParent.isGeoEnabled() || getLocation() != null) { //check if we dont need a location or if we have one
                    trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, count, trialParent, getLocation());
                    //Intent return to list view and add to trial list
                    Intent switchScreen = new Intent(ConductCountTrialActivity.this, AddTrialActivity.class);

                    //This line makes sure that this activity is not saved in the history stack
                    switchScreen.addFlags(switchScreen.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

                    switchScreen.putExtra("USER_ID", userID);
                    switchScreen.putExtra("TRIAL_PARENT", trialParent);
                    startActivity(switchScreen);
                }
                else {
                    //call toast that says you need a location
                    Toast.makeText(ConductCountTrialActivity.this, "This experiment requires a location!", Toast.LENGTH_SHORT).show();
                }

            }



        });

        //                                  LOCATION
        //---------------------------------------------------------------------------------

        //Location;
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        TrialLocationCheck(); // Check if Experiment requires location
        Button setButton = (Button) findViewById(R.id.button3);
        setButton.setVisibility(View.GONE);
        maps = new Maps();
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //exit map fragment
                setButton.setVisibility(View.GONE);
                setLocation(maps.getTrialLocation());
                getSupportFragmentManager().beginTransaction().remove(maps).commit();
                Log.i("curtis", "going back");
            }
        });
    }

    private void TrialLocationCheck() {
        Button setLocButton = (Button) findViewById(R.id.getLocButton);
        if (trialParent.isGeoEnabled()) {
            //create button to go to map fragment
            setLocButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getUserLocation();
                }
            });
        }
        else {
            setLocButton.setVisibility(View.GONE);
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("curtis", "missing perms");
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.i("curtis", location.toString());
                            setLocation(location);
                            maps.setTrialLocation(location);
                            Bundle args = new Bundle();
                            args.putParcelable("TrialLocation", getLocation());
                            args.putString("MODE", "Trial");
                            Fragment mFragment = maps;
                            mFragment.setArguments(args);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frame, mFragment).commit();
                        }
                    }
                });
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }



//                              END LOCATION
//-----------------------------------------------------------------------
    /**
     * Increments the trial result count and displays it on the activity layout
     */
    public void updateCount() {
        count += 1;
        countDisplay.setText(Integer.toString(count));
    }
}
