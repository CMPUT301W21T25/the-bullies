package com.example.cmput301w21t25;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ExperimentManager {
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
    public void FB_CreateExperiment(String name,String ownerID, String description, Location region,ArrayList<String> tags,Boolean geoEnabled, Boolean published,String type){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> experimentDoc  = new HashMap<>();
        experimentDoc.put("name",name);
        experimentDoc.put("owner",ownerID);
        experimentDoc.put("description",description);
        experimentDoc.put("region",region);
        experimentDoc.put("type", type);
        experimentDoc.put("tags",tags);
        experimentDoc.put("geoEnabled",geoEnabled);
        experimentDoc.put("published",published);
        experimentDoc.put("trialKeys", Arrays.asList());//cause an experiment should start empty

        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("Experiments")
                .add(experimentDoc)
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
    //UPDATE EXPERIMENT
    public void FB_UpdateDescription(String description,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef
                .update("description", description)
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
    public void FB_UpdateTags(ArrayList tags,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef
                .update("tags", tags)
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
    public void FB_UpdateGeoEnabled(Boolean geoEnabled,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef
                .update("geoEnabled", geoEnabled)
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
    public void FB_UpdatePublished(Boolean published,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
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
    public void FB_UpdateConductedTrials(ArrayList<String> conductedTrials,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef
                .update("trialKeys", conductedTrials)
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
