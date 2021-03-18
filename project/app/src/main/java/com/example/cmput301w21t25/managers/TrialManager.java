package com.example.cmput301w21t25.managers;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmput301w21t25.trials.Trial;
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
    public void FB_CreateCountTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,int result){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
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
    public void FB_CreateBinomialTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,boolean result){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
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
    public void FB_MeasurementTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,float result){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
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


    //METHOD THAT CHECKS WHICH TYPE OF TRIAL TO MAKE AND THEN MAKES IT
}
