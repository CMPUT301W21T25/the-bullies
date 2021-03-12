package com.example.cmput301w21t25;

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
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Experiment { //make abstract

    /****************************************
                    ATTRIBUTES
    ****************************************/
    private User owner;
    private String name;
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


    /****************************************
                CONSTRUCTORS
     ****************************************/
    public Experiment() {

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

    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes in this section are listed below
     * -YA
     * todo: update comments, add security and exceptions, complete incomplete methods,CHECK IF THIS STUFF WORKS RIGHT
     * */
    //attaching firebase good comments to come later please bear with me -YA
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //extra attributes to make ur life easier:
    private ArrayList<Trial> fb_trialList;
    private String fb_name;
    private String fb_type;

    //adding a method to make new experiment in database
    private void FB_CreateExperiment(){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String, Object> experiment = new HashMap<>();
        // Add a new Experiment with a generated ID
        db.collection("Experiments")
                .add(experiment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    //security stuff to make debuging easier
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding experiment", e);
                    }
                });
    }
    //a method to get trials associated with an experiment
    private void FB_getTrials(String experiment_ID){
        fb_trialList.clear();//makes sure list is clear
        CollectionReference colRef = db.collection("Experiments/"+experiment_ID+"/Trials");
        colRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //construct new trial
                        //store construced trial in FB_trialList
                        //what I think constructor should include: ID,name,type, value
                        //i can probably setup a case check here such that CASE A = binomeal then itll call binomeal constructor etc.
                        //this is the array of all the trials associated with this particular experiment
                        //trial dosnt need to know user experiment mangaer can figure that out itself

                        //alternativly we could upload as a trial class, not yet sure how this works requires some testing
                    }
                }
            }
        });
    }
    //a method to get the name of a trial
    public void FB_getName(String experiment_ID) {
        DocumentReference docRef = db.document("Experiments/"+experiment_ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    fb_name = (String)document.getData().get("name");
                }
            }
        });
    }
    //method to get type of trial
    public void FB_getType(String experiment_ID) {
        DocumentReference docRef = db.document("Experiments/"+experiment_ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
                    fb_type = (String)document.getData().get("type");
                }
            }
        });
    }
    public void FB_setName(String newName,String experiment_ID) {
        DocumentReference docRef = db.document("Experiments/"+experiment_ID);
        docRef.update(name,newName);
    }
    public void FB_setType(String type,String experiment_ID) {
    }
    public void FB_setKeywords(ArrayList<String> keywords,String experiment_ID) {

    }
    public void FB_setTrials(ArrayList<Trial> trials,String experiment_ID) {
    }
    public void FB_addTrial(Trial trial) {
    }
    public void FB_deleteTrial(Trial trial) {
    }
    public void FB_hideTrials() {
    }
    /**
     * End of database stuff -YA
     * */




    public ArrayList<Trial> getTrials() {
        return trials;
    }

    public ArrayList<Trial> getHiddenTrials() {
        return hiddenTrials;
    }

    public String getName() { return name; }

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
