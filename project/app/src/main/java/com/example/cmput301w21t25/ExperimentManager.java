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
    private ArrayList<Trial> fb_trialList;
    /////////////////////////////////////////////////////////////////////////////////////
    //INITIALIZE EXPERIMENT
    private void FB_CreateExperiment(String title, String ownerID, String description, Location region){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> experiment  = new HashMap<>();
        experiment.put("title",title);
        experiment.put("owner",ownerID);
        experiment.put("region",region);
        experiment.put("description",description);
        experiment.put("comment", Arrays.asList());
        experiment.put("trials",Arrays.asList());
        experiment.put("conductedTrials", Arrays.asList());
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
    /**
     * End of database stuff -YA
     * */
}
