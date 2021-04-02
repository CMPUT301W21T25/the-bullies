package com.example.cmput301w21t25.managers;

import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.FirestoreExperimentCallback;
import com.example.cmput301w21t25.FirestoreStringCallback;
import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.NonNegCountTrial;
import com.example.cmput301w21t25.trials.Trial;
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

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TrialManager {
    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes in this section are listed below
     * @author Yalmaz Abdullah
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
        //trialDoc.put("location", location);
        //experiment.put("comment", ); ill add this later

        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        DocumentReference docRef = db.collection("Experiments").document(parentExperimentID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        ArrayList<String> newKeyList = (ArrayList<String>) document.getData().get("trialKeys");
                                        newKeyList.add(documentReference.getId());
                                        expManager.FB_UpdateTrialKeys(newKeyList,parentExperimentID);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
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
 
    /**
     * This is a method that creates a Binomial Trial document in the database
     * @param ownerID this is the ID of the user who created the experiment
     * @param parentExperimentID this is the ID of the experiment this trial will be associated with
     * @param parentExperimentName this is the name of the experiment this trial will be associated with
     * @param parentExperimentOwnerName this is the name of the owner of the experiment
     * @param published this is a boolean to show weather the trial is published or not
     * @param result this is the result of the trial that you want to store
     * @param parent this is the parent experiment object used to update the list of trial keys stored in the experiment
     */
    public void FB_CreateBinomialTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,boolean result,Experiment parent, Location location){

        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentID",parent.getFb_id());
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
        trialDoc.put("date", new Date());
        trialDoc.put("location", location);
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        ArrayList<String> newKeyList = parent.getTrialKeys();
//                        newKeyList.add(documentReference.getId());
//                        expManager.FB_UpdateConductedTrials(newKeyList,parentExperimentID);

                        DocumentReference docRef = db.collection("Experiments").document(parentExperimentID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        ArrayList<String> newKeyList = (ArrayList<String>) document.getData().get("trialKeys");
                                        newKeyList.add(documentReference.getId());
                                        expManager.FB_UpdateTrialKeys(newKeyList,parentExperimentID);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
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

    /**
     * This is a method that creates a Measurement Trial document in the database
     * @param ownerID this is the ID of the user who created the experiment
     * @param parentExperimentID this is the ID of the experiment this trial will be associated with
     * @param parentExperimentName this is the name of the experiment this trial will be associated with
     * @param parentExperimentOwnerName this is the name of the owner of the experiment
     * @param published this is a boolean to show weather the trial is published or not
     * @param result this is the result of the trial that you want to store
     * @param parent this is the parent experiment object used to update the list of trial keys stored in the experiment
     */
    public void FB_CreateMeasurementTrial(String ownerID,String parentExperimentID,String parentExperimentName,String parentExperimentOwnerName, boolean published,float result,Experiment parent){
        // Create a new experiment Hash Map this is the datatype stored in firebase for documents
        Map<String,Object> trialDoc  = new HashMap<>();
        trialDoc.put("user",ownerID);
        trialDoc.put("experiment",parentExperimentID);
        trialDoc.put("experimentName",parentExperimentName);
        trialDoc.put("experimentOwnerName",parentExperimentOwnerName);
        trialDoc.put("published",published);
        trialDoc.put("result",result);
        trialDoc.put("date", new Date());
        //trialDoc.put("location", location);
        //experiment.put("comment", ); ill add this later

        // Add a new Experiment with a generated ID
        db.collection("TrialDocs")
                .add(trialDoc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        ArrayList<String> newKeyList = parent.getTrialKeys();
//                        newKeyList.add(documentReference.getId());
//                        expManager.FB_UpdateConductedTrials(newKeyList,parentExperimentID);

                        DocumentReference docRef = db.collection("Experiments").document(parentExperimentID);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        ArrayList<String> newKeyList = (ArrayList<String>) document.getData().get("trialKeys");
                                        newKeyList.add(documentReference.getId());
                                        expManager.FB_UpdateTrialKeys(newKeyList,parentExperimentID);
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
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
     *
     * @param exp
     * @param trialAdapter
     * @param trials
     */
    public void FB_UpdateTrialAdapter(Experiment exp, ArrayAdapter<Trial> trialAdapter, ArrayList<Trial> trials){
        expManager.FB_FetchTrialKeys(exp.getFb_id(), new FirestoreStringCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if(list.size()>0){
                    Log.d("YA-DB TEST: ", "calling the fetch" );
                    db.collection("TrialDocs").whereIn(FieldPath.documentId(),list)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    trials.clear();
                                    trialAdapter.notifyDataSetChanged();
                                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                    {
                                        if (doc.exists()&& !((Boolean) doc.getData().get("published"))) {
                                            switch (exp.getType()) {
                                                case "binomial":
                                                    //ArrayList<Experiment>test = new ArrayList<Experiment>();
                                                    BinomialTrial binTrial = doc.toObject(BinomialTrial.class);
                                                    binTrial.setTrialId(doc.getId());
                                                    trials.add(binTrial);
                                                    trialAdapter.notifyDataSetChanged();
                                                    break;
                                                case "count":
                                                    CountTrial countTrial = doc.toObject(CountTrial.class);
                                                    countTrial.setTrialId(doc.getId());
                                                    trials.add(countTrial);
                                                    trialAdapter.notifyDataSetChanged();
                                                    Log.d("YA-DB: ", String.valueOf(trials));
                                                    break;
                                                case "nonnegative count":
                                                    NonNegCountTrial nnCountTrial = doc.toObject(NonNegCountTrial.class);
                                                    nnCountTrial.setTrialId(doc.getId());
                                                    trials.add(nnCountTrial);
                                                    trialAdapter.notifyDataSetChanged();
                                                    break;
                                                case "measurement":
                                                    MeasurementTrial mesTrial = doc.toObject(MeasurementTrial.class);
                                                    mesTrial.setTrialId(doc.getId());
                                                    trials.add(mesTrial);
                                                    trialAdapter.notifyDataSetChanged();
                                                    break;
                                                default:
                                                    Log.d("YA-DB: ", "this experiment was not assigned the correct class when it was uploaded so i dont know what class to make");
                                            }
                                        }
                                    }
                                }
                            });
                }

            }
        });
    }
    public void FB_FetchPublishedTrialCount(Experiment exp, TextView currTrials){
        expManager.FB_FetchTrialKeys(exp.getFb_id(), new FirestoreStringCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                if(list.size()>0){
                    Log.d("YA-DB TEST: ", "calling the fetch" );
                    db.collection("TrialDocs").whereIn(FieldPath.documentId(),list).whereEqualTo("published",true)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                    List<DocumentSnapshot> temp = queryDocumentSnapshots.getDocuments();
                                    int size = temp.size();
                                    currTrials.setText("Current Trials: " + String.valueOf(size));
                                }
                            });
                }

            }
        });
    }
    public void FB_FetchPublishedTrial(Experiment exp, FirestoreTrialCallback firestoreTrialCallback){
        expManager.FB_FetchTrialKeys(exp.getFb_id(), new FirestoreStringCallback() {
            @Override
            public void onCallback(ArrayList<String> list) {
                ArrayList<Trial> trialList = new ArrayList<Trial>();
                db.collection("TrialDocs").whereIn(FieldPath.documentId(),list).whereEqualTo("published",true)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                switch(exp.getType()){
                                    case "count":
                                        CountTrial countParent = doc.toObject(CountTrial.class);
                                        countParent.setTrialId(doc.getId());
                                        //Log.d("YA_TEST:",countParent.getTrialId());
                                        trialList.add(countParent);
                                        break;
                                    case "binomial":
                                        BinomialTrial binomialParent = doc.toObject(BinomialTrial.class);
                                        binomialParent.setTrialId(doc.getId());
                                        //Log.d("YA_TEST:",binomialParent.getTrialId());
                                        trialList.add(binomialParent);
                                        break;
                                    case "nonnegative count":
                                        NonNegCountTrial nnCountParent = doc.toObject(NonNegCountTrial.class);
                                        nnCountParent.setTrialId(doc.getId());
                                        //Log.d("YA_TEST:",nnCountParent.getTrialId());
                                        trialList.add(nnCountParent);
                                        break;
                                    case "measurement":
                                        MeasurementTrial measurementParent = doc.toObject(MeasurementTrial.class);
                                        measurementParent.setTrialId(doc.getId());
                                        //Log.d("YA_TEST:",measurementParent.getTrialId());
                                        trialList.add(measurementParent);
                                        break;
                                }
                            }
                            firestoreTrialCallback.onCallback(trialList);
                        }
                    });
            }
        });
    }

    /**
     * End of database stuff -YA
     * */


}
