package com.example.cmput301w21t25;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TrialManager {
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
    /////////////////////////////////////////////////////////////////////////////////////
    //INITIALIZE EXPERIMENT
    public void FB_CreateTrial(String ownerID, String parentExperimentID, Location geoLocation, boolean published, boolean hidden,String type){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("geoLocation",geoLocation);
        trialDoc.put("published",published);
        trialDoc.put("hidden",hidden);
        trialDoc.put("type",type);
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
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
    //overloaded for trials that dont have geolocation enabled
    public void FB_CreateTrial(String ownerID,String parentExperimentID, boolean published,boolean hidden,String type){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("published",published);
        trialDoc.put("hidden",hidden);
        trialDoc.put("type",type);
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
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
    /////////////////////////////////////////////////////////////////////////////////////
    //UPDATE TRIAL
    public void FB_UpdateTrial(Trial trial,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("trial", trial)
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
    public void FB_UpdateHidden(boolean hidden,String id){
        DocumentReference docRef = db.collection("TrialDocs").document(id);
        docRef
                .update("hidden", hidden)
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
     * End of database stuff -YA
     * */


    //METHOD THAT CHECKS WHICH TYPE OF TRIAL TO MAKE AND THEN MAKES IT
}
