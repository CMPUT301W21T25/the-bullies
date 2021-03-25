package com.example.cmput301w21t25.managers;

import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.FirestoreCallback;
import com.example.cmput301w21t25.custom.CustomListExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.google.android.gms.measurement.api.AppMeasurementSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ExperimentManager {
    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes in this section are listed below
     * @author:Yalmaz Abdullah
     * todo: update comments, add security and exceptions, complete incomplete methods,CHECK IF THIS STUFF WORKS RIGHT
     * */
    //
    // ATTRIBUTES
    //
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String fb_expID = "";
    private UserManager userManager= new UserManager();
    //
    // CREATE EXPERIMENT
    //
    /**
     * This method creates an Experiment Document in the database that can later be recompiled into one of the Experiment Class's children
     * @param ownerID the ID of the user who is creating the experiment
     * @param experimentName the name of the experiment
     * @param ownerName the name of the owner of the experiment
     * @param description a brief description of the experiment
     * @param region the geolocation of the region
     * @param tags the tags associated with the experiment
     * @param geoEnabled a boolean that tells weather or not geolocation is enabled or not
     * @param published a boolean that tells weather or not the experiment is published or not
     * @param type the type of experiment(ie: count, nonNegCount, binomial etc)
     * @param date the date and time of creation of the experiment
     * @param minTrials the minimum number of trials required to end an experiment
     * @param pubTrials the current number of published trials an experiment has
     */
    public void FB_CreateExperiment(String ownerID, String experimentName, String ownerName,
                                    String description, Location region, ArrayList<String> tags,
                                    Boolean geoEnabled, Boolean published, String type, Date date,
                                    int minTrials, int pubTrials){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> experimentDoc  = new HashMap<>();
        experimentDoc.put("ownerID", ownerID);
        experimentDoc.put("name",experimentName);
        experimentDoc.put("minNumTrials", minTrials);
        experimentDoc.put("publishedTrials", pubTrials);
        experimentDoc.put("owner",ownerName);
        experimentDoc.put("description",description);
        experimentDoc.put("region",region);
        experimentDoc.put("type", type);
        experimentDoc.put("tags",tags);
        experimentDoc.put("date",date);
        experimentDoc.put("geoEnabled",geoEnabled);
        experimentDoc.put("published",published);
        experimentDoc.put("isEnded",false);
        experimentDoc.put("trialKeys", Arrays.asList());//cause an experiment should start empty

        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("Experiments")
                .add(experimentDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        db.collection("UserProfile").document(ownerID).get()

                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                ArrayList<String> currentOwned = (ArrayList<String>)document.getData().get("ownedExperiments");
                                                currentOwned.add(documentReference.getId());

                                                userManager.FB_UpdateOwnedExperiments(currentOwned,ownerID);
                                                userManager.FB_UpdateSubscriptions(currentOwned,ownerID);

                                            }
                                        }
                                    }
                                });

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
    //
    // UPDATE EXPERIMENT
    //
    /**
     * this method updates the name of owner stored in the database by getting the user name o
     * @param ownerID id of the user whose name is to be updated
     */
    public void FB_UpdateName(String ownerID,String name) {
        //needs an owner id to update their experiments
        //grabs their current name, changes them on experiments
        db.collection("Experiments")
                .whereEqualTo("ownerID", ownerID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                document.getReference().update("owner", name);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    /**
     * This method updates the description of the experiment
     * @param description new description
     * @param id id of the experiment you want to update
     */
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
    /**
     * This method updates the tags of an experiment
     * @param tags the new list of tags
     * @param id the id of the experiment you want to update
     */
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
    /**
     * This method updates the geoEnabled boolean
     * @param geoEnabled this is the new geoEnabled boolean
     * @param id this is id of the experiment you want to update
     */
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

    /**
     * This method updates the published boolean of the experiment
     * @param published this is the new published boolean
     * @param id this is the id of the experiment you want to update
     */
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
    /**
     * This method updates the published boolean of the experiment
     * @param ended this is the new isEnded boolean
     * @param id this is the id of the experiment you want to update
     */
    public void FB_UpdateEnded(Boolean ended,String id){
        DocumentReference docRef = db.collection("Experiments").document(id);
        docRef
                .update("isEnded", ended)
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
     * This method updates the list of trial keys
     * @param conductedTrials this is the new list of conducted trials
     * @param id this is the id of experiment you want to update
     */
    public void FB_UpdateTrialKeys(ArrayList<String> conductedTrials,String id){
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
    //
    // FETCH EXPERIMENTS
    //
    public void FB_UpdateOwnedExperimentAdapter(String userID, ArrayAdapter<Experiment> experimentAdapter, ArrayList<Experiment> experiments){
        userManager.FB_FetchOwnedExperimentKeys(userID, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if(list.size()>0){
                    Log.d("YA-DB: ", "calling the fetch" );
                    db.collection("Experiments").whereIn(FieldPath.documentId(),list)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    experiments.clear();
                                    experimentAdapter.notifyDataSetChanged();
                                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                    {
                                        Experiment experiment = doc.toObject(Experiment.class);
                                        experiment.setFb_id(doc.getId());
                                        experiments.add(experiment);
                                        experimentAdapter.notifyDataSetChanged();
                                        Log.d("YA-DB: ", "fetched: " + experiments);
                                    }
                                }
                            });
                }

            }
        });

    }
    public void FB_UpdateSubbedExperimentAdapter(String userID, ArrayAdapter<Experiment> experimentAdapter, ArrayList<Experiment> experiments){
        userManager.FB_FetchSubbedExperimentKeys(userID, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if(list.size()>0){
                    Log.d("YA-DB: ", "calling the fetch" );
                    db.collection("Experiments").whereIn(FieldPath.documentId(),list)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    experiments.clear();
                                    experimentAdapter.notifyDataSetChanged();
                                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                    {
                                        Experiment experiment = doc.toObject(Experiment.class);
                                        experiment.setFb_id(doc.getId());
                                        experiments.add(experiment);
                                        experimentAdapter.notifyDataSetChanged();
                                        Log.d("YA-DB: ", "fetched: " + experiments);
                                    }
                                }
                            });
                }

            }
        });
    }
    public void FB_UpdateBrowseExperimentAdapter(String userID, ArrayAdapter<Experiment> experimentAdapter, ArrayList<Experiment> experiments){
        userManager.FB_FetchSubbedExperimentKeys(userID, new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if(list.size()>0){
                    Log.d("YA-DB: ", "calling the fetch" );
                    db.collection("Experiments").whereNotIn(FieldPath.documentId(),list).whereEqualTo("published", true)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    experiments.clear();
                                    experimentAdapter.notifyDataSetChanged();
                                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                    {
                                        Experiment experiment = doc.toObject(Experiment.class);
                                        experiment.setFb_id(doc.getId());
                                        experiments.add(experiment);
                                        experimentAdapter.notifyDataSetChanged();
                                        Log.d("YA-DB: ", "fetched: " + experiments);
                                    }
                                }
                            });
                }

            }
        });
    }
    public void FB_UpdateExperimentTextViews(String expID, TextView expName,TextView expDesc,TextView expType,TextView minTrials,TextView currTrials){
        db.collection("Experiments").whereEqualTo(FieldPath.documentId(),expID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            Experiment experiment = doc.toObject(Experiment.class);
                            experiment.setFb_id(doc.getId());
                            expName.setText(experiment.getName());
                            expDesc.setText(experiment.getDescription());
                            expType.setText(experiment.getType());
                            minTrials.setText("Minimum Trials: " + String.valueOf(experiment.getMinNumTrials()));
                            currTrials.setText("Current Trials: " + String.valueOf(experiment.getCurrentNumTrials()));
                            Log.d("YA_DB test: ", "fetched: " + experiment);
                        }
                    }
                });
    }
    /**
     * End of database stuff -YA
     * */
}
