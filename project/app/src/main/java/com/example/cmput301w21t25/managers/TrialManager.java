package com.example.cmput301w21t25.managers;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TrialManager {
    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes in this section are listed below
     * @author:Yalmaz Abdullah
     * todo: update comments, add security and exceptions, complete incomplete methods,CHECK IF THIS STUFF WORKS RIGHT
     * */
    //ATTRIBUTES
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //extra attributes to make ur life easier:
    /////////////////////////////////////////////////////////////////////////////////////
    //INITIALIZE EXPERIMENT
    private ExperimentManager expManager = new ExperimentManager();//this is used to update the list of trial keys associated of the parent experiment

    //CREATE TRIALS
    /**
     * This is a method that creates a Count Trial document in the database (this is used to store NonNegCount Trials) it also updates the associated Experiment's list of trial keys
     * @param ownerID this is the ID of the user who created the experiment
     * @param parentExperimentID this is the ID of the experiment this trial will be associated with
     * @param parentExperimentName this is the name of the experiment this trial will be associated with
     * @param parentExperimentOwnerName this is the name of the owner of the experiment
     * @param published this is a boolean to show weather the trial is published or not
     * @param result this is the result of the trial that you want to store
     * @param parent this is the parent experiment object used to update the list of trial keys stored in the experiment
     */
    public void FB_CreateCountTrial(String ownerID, String parentExperimentID, String parentExperimentName, String parentExperimentOwnerName, boolean published, int result, Experiment parent){

        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
        trialDoc.put("date", new Date());
        //experiment.put("comment", ); ill add this later


        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        ArrayList<String> newKeyList = parent.getTrialKeys();
                        newKeyList.add(documentReference.getId());
                        expManager.FB_UpdateConductedTrials(newKeyList,parentExperimentID);
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

    public void FB_CreateBinomialTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,boolean result,Experiment parent){
 
    /**
     * This is a method that creates a Binomial Trial document in the database
     * @param ownerID this is the ID of the user who created the experiment
     * @param parentExperimentID this is the ID of the experiment this trial will be associated with
     * @param parentExperimentName this is the name of the experiment this trial will be associated with
     * @param parentExperimentOwnerName this is the name of the owner of the experiment
     * @param published this is a boolean to show weather the trial is published or not
     * @param result this is the result of the trial that you want to store
     * @param parent this is the parent experiment object used to update the list of trial keys stored in the experiment
     * @param date this is the date that the trial was created
     */

        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
        trialDoc.put("date", new Date());
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        ArrayList<String> newKeyList = parent.getTrialKeys();
                        newKeyList.add(documentReference.getId());
                        expManager.FB_UpdateConductedTrials(newKeyList,parentExperimentID);
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

    public void FB_CreateMeasurementTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,float result,Experiment parent){

    /**
     * This is a method that creates a Measurement Trial document in the database
     * @param ownerID this is the ID of the user who created the experiment
     * @param parentExperimentID this is the ID of the experiment this trial will be associated with
     * @param parentExperimentName this is the name of the experiment this trial will be associated with
     * @param parentExperimentOwnerName this is the name of the owner of the experiment
     * @param published this is a boolean to show weather the trial is published or not
     * @param result this is the result of the trial that you want to store
     * @param parent this is the parent experiment object used to update the list of trial keys stored in the experiment
     * @param date this is the date the trial was created
     */

        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
        trialDoc.put("date", new Date());
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        ArrayList<String> newKeyList = parent.getTrialKeys();
                        newKeyList.add(documentReference.getId());
                        expManager.FB_UpdateConductedTrials(newKeyList,parentExperimentID);
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

    //UPDATE THE TRIALS
    /**
     * Updates the published field in the database for the trial associated with the provided ID
     * @param published this is the boolean that you want to change the published field to
     * @param id this is the ID of the trial you want to update
     */
    public void FB_UpdatePublished(boolean published,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("published", published)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    /**
     * Update the results for a Count Trial or NonNegCount Trial
     * @param result the new result
     * @param id the ID of the trial you want to update
     */
    public void FB_UpdateCountResult(int result,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("result", result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    /**
     * Update the results for a Binomial Trial
     * @param result the new result
     * @param id the ID of the trial you want to update
     */
    public void FB_UpdateBinomialResult(boolean result,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("published", result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    /**
     * Update the results for a Measurement Trial
     * @param result the new result
     * @param id the ID of the trial you want to update
     */
    public void FB_UpdateMeasurementResult(float result,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("published", result)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    /**
     * End of database stuff -YA
     * */
}
