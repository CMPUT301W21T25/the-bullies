package com.example.cmput301w21t25.managers;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.FirestoreCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.user.User;
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
import java.util.HashMap;
import java.util.Map;

public class UserManager{
    /**
     * please look over this before continuing work
     * Database stuff starting here ill add proper comments later in a bit of a rush atm
     * methods to get and set to database use as u please attributes used in this section are listed below
     * @author Yalmaz Abdullah
     * todo:
     *  -Usermangaer can make new user doc in DB(Done)
     *  -Usermanager can delete existing doc in DB(Done)
     *  -Usermanager can summon user doc in DB():
     *      -get user name()
     *      -get user email()
     *      -get user class()
     * */
    //ATTRIBUTES
    private FirebaseFirestore db;

    public UserManager() {
        db = FirebaseFirestore.getInstance();
    }

    public UserManager(FirebaseFirestore db) {
        this.db = db;
    }

    //INITIALIZE PROFILE

    /**
     * This method makes a database entery for the User profile
     * @param id this is the FireBaseInstallationID of the phone
     * @param name this is the name of the user
     * @param email this is the email of the user
     * @param user this is the User object (to be removed after refactoring)
     */
    public void FB_CreateUserProfile(String id, String name, String email, User user){
        Map<String,Object> userprofile  = new HashMap<>();
        userprofile.put("name",name);
        userprofile.put("email",email);
        //userprofile.put("user",user);
        userprofile.put("subscriptions", Arrays.asList());
        userprofile.put("ownedExperiments",Arrays.asList());
        userprofile.put("conductedTrials", Arrays.asList());

        db.collection("UserProfile").document(id).set(userprofile)
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
    //MAKE SURE USERNAME AND EMAIL ARE UNIQUE

    /**
     * @author Curtis Kennedy
     * this ensures that the user name and email are unique
     * @param text this is the string value the function will check the uniqueness of (ie: the input username or the input email)
     * @param type this is the a string that tells the function weather its checking for email or username
     * @return
     */
    public boolean FB_isUnique(String text, String type) {//is this working?
        final boolean[] success = {true};
        //text is the text that you want to make sure is unique, like a username
        //type is either "userName" or "email" to indicate what are to check for uniqueness
        if (type.equals("userName")) {
            //username check
            Log.i("TYPE OF CHECK", "USERNAME");
            db.collection("UserProfile")
                    .whereEqualTo("name", text)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //fail
                                Log.i("USERNAME CHECK", "USERNAME IS NOT UNIQUE");
                                success[0] = false;
                            }
                            else {
                                //yay this name is unique
                                Log.i("USERNAME CHECK", "USERNAME IS UNIQUE");
                                success[0] = true;
                            }
                        }
                    });
        }
        else {
            //email check
            Log.i("TYPE OF CHECK", "EMAIL");
            db.collection("UserProfile")
                    .whereEqualTo("email", text)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                //fail
                                Log.i("EMAIL CHECK", "EMAIL IS NOT UNIQUE");
                                success[0] = false;
                            }
                            else {
                                //yay this name is unique
                                Log.i("EMAIL CHECK", "EMAIL IS UNIQUE");
                                success[0] = true;
                            }
                        }
                    });
        }
        return success[0];
    }

    //UPDATE PROFILE
    /**
     * This method updates the user name of the associated ID
     * @param name new user name
     * @param id the ID of the user you want to update
     */
    public void FB_UpdateName(String name,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("name", name)
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
     * This method updates the email of the associated ID
     * @param email the new email of the user
     * @param id the ID of the user you want to update
     */
    public void FB_UpdateEmail(String email,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("email", email)
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
     * This method updates the list of keys of experiments this user is subscribed to
     * @param subscriptions new list of experiment keys
     * @param id the ID of the user you want to update
     */
    public void FB_UpdateSubscriptions(ArrayList subscriptions,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("subscriptions", subscriptions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("YA-DB:","updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    /**
     * This method updates the list of keys of experiments the user owns
     * @param owned new list of experiment keys
     * @param id the ID of the user you want to update
     */
    public void FB_UpdateOwnedExperiments(ArrayList owned,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("ownedExperiments", owned)
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
     * This method updates the list of keys of trials the user has conducted
     * @param trialKeys new list of experiment keys
     * @param id the ID of the user you want to update
     */
    public void FB_UpdateConductedTrials(ArrayList trialKeys,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("conductedTrials", trialKeys)
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
     * @param id
     * @param fsCallback
     */
    //this function is a test function to pull the keys synchronously, they dont but were close enough
    public void FB_FetchOwnedExperimentKeys(String id,FirestoreCallback fsCallback){//the fsCallback is an object that functions similarly to a wait function
        db.collection("UserProfile").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if(doc != null && doc.exists()){
                    ArrayList<String> key = (ArrayList<String>) doc.get("ownedExperiments");
                    Log.d("YA-DB-Rev2 inner:", String.valueOf(key)+" "+ System.currentTimeMillis());
                    fsCallback.onCallback(key);
                }
            }
        });
    }

    /**
     *
     * @param id
     * @param fsCallback
     */
    public void FB_FetchSubbedExperimentKeys(String id,FirestoreCallback fsCallback){//the fsCallback is an object that functions similarly to a wait function
        db.collection("UserProfile").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if(doc != null && doc.exists()){
                    ArrayList<String> key = (ArrayList<String>) doc.get("subscriptions");
                    Log.d("YA-DB-Rev2 inner:", String.valueOf(key)+" "+ System.currentTimeMillis());
                    fsCallback.onCallback(key);
                }
            }
        });
    }

    /**
     * This method retrieves the email and name of a specified user, with a high potential to be expanded
     * @param id the id of the user to retrieve
     * @param fsCallback the callback for receiving data
     */
    public void FB_FetchUserInfo(String id, FirestoreCallback fsCallback) {
        db.collection("UserProfile").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (doc != null && doc.exists()){
                    ArrayList<String> key = new ArrayList<String>();
                    key.add(doc.get("email").toString());
                    key.add(doc.get("name").toString());
                    fsCallback.onCallback(key);
                }
            }
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //since we can update trials and stuff i dont see the need of making delete functions, if u want them ill put them in
    /**
     * End of database stuff -YA
     * */
}
