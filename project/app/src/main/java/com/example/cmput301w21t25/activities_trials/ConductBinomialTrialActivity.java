package com.example.cmput301w21t25.activities_trials;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
 * Conduct binomial trial activity, allows experimenter to increment count. On completion, sends
 * trial to database as doc, returns to add trial list view.
 */
public class ConductBinomialTrialActivity extends AppCompatActivity {

    Toolbar trialHeader;
    Button successButton;
    Button failureButton;
    TextView description;

    private String userID;
    private Experiment trialParent;

    TrialManager trialManager;

    //Location
    private FusedLocationProviderClient locationClient;
    private Location location;
    private Maps maps;
    //End location

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_conduct_binomial_trial);

        //Grab user ID
        userID = getIntent().getStringExtra("USER_ID");
        //Pass experiment object to access attributes
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        //Set up trial manager
        trialManager = new TrialManager();

        trialHeader = findViewById(R.id.binomialExperimentInfo);
        description = findViewById(R.id.binomialExpDescription);
        successButton = findViewById(R.id.binomialSuccessButton);
        failureButton = findViewById(R.id.binomialFailureButton);

        //Display Experiment info on conduct Trial page
        trialHeader.setTitle(trialParent.getName());
        trialHeader.setSubtitle(trialParent.getOwner());
        description.setText(trialParent.getDescription());

        //On click, assign True or False value of Boolean Experiment
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call function that creates trial with result and switches activity
                if (!trialParent.isGeoEnabled() || getLocation() != null) { // if location is not required or we have a location {
                    GeoPoint geoPoint = new GeoPoint(getLocation().getLatitude(), getLocation().getLongitude());
                    trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, true, trialParent, geoPoint);
                    Intent switchScreen = new Intent(ConductBinomialTrialActivity.this, AddTrialActivity.class);
                    //Passes the parent Experiment back as it is needed in the add Trial list view
                    switchScreen.putExtra("USER_ID", userID);
                    switchScreen.putExtra("TRIAL_PARENT", trialParent);
                    startActivity(switchScreen);
                }
                else {
                    //call toast that says you need a location
                    Toast.makeText(ConductBinomialTrialActivity.this, "This experiment requires a location!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //On click, confirm trial, return to trial list view
        failureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call function that creates trial with result and switches activity
                if (!trialParent.isGeoEnabled() || getLocation() != null) { // if location is not required or we have a location
                    //Since get returns null if we dont have a location, create a null geopoint instead
                    GeoPoint geoPoint = null;
                    if (trialParent.isGeoEnabled()) {
                        geoPoint = new GeoPoint(getLocation().getLatitude(), getLocation().getLongitude());
                    }
                    trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, false, trialParent, geoPoint);
                    Intent switchScreen = new Intent(ConductBinomialTrialActivity.this, AddTrialActivity.class);
                    switchScreen.putExtra("USER_ID", userID);
                    switchScreen.putExtra("TRIAL_PARENT", trialParent);
                    startActivity(switchScreen);
                }
                else {
                    //call toast that says you need a location
                    Toast.makeText(ConductBinomialTrialActivity.this, "This experiment requires a location!", Toast.LENGTH_SHORT).show();
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
