package com.example.cmput301w21t25.managers;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cmput301w21t25.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    /////////////////////////////////////////////////////////////////////////////////////
    //since we can update trials and stuff i dont see the need of making delete functions, if u want them ill put them in
    /**
     * End of database stuff -YA
     * */
}
