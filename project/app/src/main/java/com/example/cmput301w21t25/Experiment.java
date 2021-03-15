package com.example.cmput301w21t25;

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
import java.util.ArrayList;
//abstract
public class Experiment { //make abstract

    /****************************************
                    ATTRIBUTES
    ****************************************/
    private User owner;
    public String name;
    private String description;
    private String type;
    private ArrayList<String> keywords;
    private int minNumTrials;
    private boolean isPublished = false;

    private ArrayList<User> subscribedUsers = new ArrayList<User>(); //all currently subscribed users
    private ArrayList<User> allUsers = new ArrayList<User>(); //for users that were subscribed, added data, and unsubscribed
    private ArrayList<Trial> trials = new ArrayList<Trial>();
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
    public Experiment(String name) {
        this.name = name;
    }
    public Experiment(User owner, String description, int minNumTrials) {
        this.owner = owner;
        this.description = description;
        this.minNumTrials = minNumTrials;
    }


    /****************************************
                    METHODS
    ****************************************/
    //private ArrayList<Comment> forum;

    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public ArrayList<Trial> getHiddenTrials() {
        return hiddenTrials;
    }

    public String getName() {
        return name;
    }
    public String retTest(){
        return "test";
    }

    public String getType() { return type; }

    public ArrayList<String> getKeywords() { return keywords; }

    public void setName(String name) { this.name = name; }

    public void setType(String type) { this.type = type; }

    public void setKeywords(ArrayList<String> keywords) { this.keywords = keywords; }

    public void setTrials(ArrayList<Trial> trials) {
        this.trials = trials;
    }

    public void addTrial(Trial trial) {
        trials.add(trial);
    }

    public void deleteTrial(Trial trial) {
        trials.remove(trial);
    }

    /**
     * Adds a user to the list of all users and the list of subscribed users,
     * for use when a user subscribes to an experiment.
     * @param user
     *      The user to be added
     */
    public void addUser(User user) {
        allUsers.add(user);
        subscribedUsers.add(user);
    }

    /**
     * Deletes given user from list of currently subscribed users
     * @param user
     *      The user to be deleted
     */
    public void deleteUser(User user) {
        subscribedUsers.remove(user);
    }

    /**
     * Allows experiment owner to hide trial results submitted by a given user.
     * Throws exception if user's trials are already hidden. //Do we need to though?
     * @param user
     *      The user who's trials the owner wants to hide
     */
    public void hideTrials(User user) throws IllegalArgumentException{

        // throw exception if user not present in trials
        // or if user present in hiddenTrials?

        for (Trial trial : trials) {
            if (trial.getUser() == user) {
                hiddenTrials.add(trial);
            }
        }
        trials.removeAll(hiddenTrials); //this might be inefficient?
    }

    /**
     * Allows experiment owner to show hidden trial results submitted by a given user.
     * Throws exception if user's trials are already shown. //Again, is this necessary?
     * @param user
     *      The user who's trials the owner wants to show
     */
    void showTrials(User user) {
        for (Trial trial : hiddenTrials) {
            if (trial.getUser() == user) {
                trials.add(trial);
            }
        }

        hiddenTrials.removeAll(trials); //this might be inefficient?

    }

    /**
     * Shows all hidden experiment trials
     */
    void showAllTrials() {
        for (Trial trial : hiddenTrials) {
            trials.add(trial);
        }
        hiddenTrials.clear();
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
