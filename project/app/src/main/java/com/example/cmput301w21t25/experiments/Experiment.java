package com.example.cmput301w21t25.experiments;

/* OLD IMPORTS THAT ANDROID STUDIO AXED THAT MAYBE WE STILL NEED LATER???
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
 */
import android.location.Location;

import com.example.cmput301w21t25.trials.Trial;
import com.example.cmput301w21t25.user.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * this class is an Abstract class for experiments
 * it sets up the most important methods for the classes that the children will inherit
 */
public class Experiment implements Serializable {

    /****************************************
                    ATTRIBUTES
    ****************************************/

    private boolean isPublished = false;
    private boolean published = false;

    private String fb_id = "";
    
    private boolean geoEnabled;
    private boolean isEnded = false;
    private int minNumTrials;
    
    public String name;
    private String type;
    private String description;
    
    //Should we just be passing in a User object that has a name and ID associated with it?
    private String owner;
    private String ownerID;
  
    private Date date;
    private Location region;
  
    private int currentNumTrials;
    private ArrayList<String> tags;
    private ArrayList<String> trialKeys = new ArrayList<String>();
    private ArrayList<Trial> hiddenTrials = new ArrayList<Trial>();

    //private Forum forum;

    //private Region region;
    //private ArrayList<Region> geoLocations;
    //attributes that have public getters automatically become part of the object when converting from DB Map

    /****************************************
                CONSTRUCTORS
     ****************************************/
    public Experiment() {
    }
    /****************************************
                    METHODS
    ****************************************/

    ///setter and getter for the experiment ID
    public String getFb_id(){ return this.fb_id; }
    public void setFb_id(String id){ this.fb_id = id; }
    ///setter and getter for Date
    public void setDate(Date date) { this.date = date; }
    public Date getDate() { return date; }
    ///getters for FireBase data
    public String getDescription() { return description; }
    public boolean getIsEnded() {
        return isEnded;
    }
    public int getMinNumTrials() { return minNumTrials; }
    public String getName() {
        return name;
    }

    public String getOwner() { return owner; }
    public String getOwnerID() { return ownerID; }
    public boolean getPublished(){return published;}
    public ArrayList<String> getTrialKeys() {
        return trialKeys;
    }


    // Do we need this?
    public String retTest(){
        return "test";
    }
    public ArrayList<String> getTags() { return tags; }
    public String getType() { return type; }

    ///Extra setters for testing
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setTags(ArrayList<String> tags) { this.tags = tags; }
    public void setDescription(String description) { this.description = description; }
    public void setCurrentNumTrials(int currentNumTrials) {
        this.currentNumTrials = currentNumTrials;
    }
    public int getCurrentNumTrials() { return currentNumTrials; }

    public boolean isGeoEnabled() {
        return geoEnabled;
    }

    public void setGeoEnabled(boolean geoEnabled) {
        this.geoEnabled = geoEnabled;
    }


    /**
     * Shows stats of experiment
     */
    void showStats() {

    }

    /**
     * Plots histogram (and maybe other plots) of experiment data
     */
    void plotData() {

    }

    /**
     * Shows a map of trial geolocations
     */
    void showMap() {

    }

}
