package com.example.cmput301w21t25.activities_trials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.firestore.GeoPoint;

/**
 * @author Eden
 * this activity is used to conduct non negative count trial experiments On completion, sends
 *trial to database as doc, returns to add trial list view.
 */
public class ConductNonnegativeCountTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button submitTrialButton;
    EditText countDisplay;
    TextView description;

    private Experiment trialParent;
    private TrialManager trialManager;
    private String userID;
    private String countString;
    private int count;

    //Location
    private FusedLocationProviderClient locationClient;
    private Location location;
    private Maps maps;
    //End location

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_nonnegative_count_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        //Initialize TrialManager
        trialManager = new TrialManager();

        //We likely need to fetch what the experiment is out of? E.g., out of ___ eggs in a carton,
        //how many cracked when you dropped the carton?

        trialHeader = findViewById(R.id.nonnegativeCountExperimentInfo);
        submitTrialButton = findViewById(R.id.submit_trial_nonnegative_count_button);
        countDisplay = findViewById(R.id.nonnegativeCountEntry);
        description = findViewById(R.id.nonnegativeExpDescription);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        Toast toast = Toast.makeText(getApplicationContext(), "The count number are required", Toast.LENGTH_LONG);

        //On click, confirm trial, return to trial list view
        submitTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(countDisplay.getText().length()>0){
                    if (!trialParent.isGeoEnabled() || getLocation() != null) { //check if we dont need a location or if we have one
                        //Intent return to list view and add to trial list
                        countString = countDisplay.getText().toString();
                        count = Integer.parseInt(countString);
                        //Since get returns null if we dont have a location, create a null geopoint instead
                        GeoPoint geoPoint = null;
                        if (trialParent.isGeoEnabled()) {
                            geoPoint = new GeoPoint(getLocation().getLatitude(), getLocation().getLongitude());
                        }
                        trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, count, trialParent, geoPoint);
                        Intent switchScreen = new Intent(ConductNonnegativeCountTrialActivity.this, AddTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", trialParent);
                        startActivity(switchScreen);
                    }
                    else {
                        //call toast that says you need a location
                        Toast.makeText(ConductNonnegativeCountTrialActivity.this, "This experiment requires a location!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    toast.show();
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

}
