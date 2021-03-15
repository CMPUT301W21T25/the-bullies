package com.example.cmput301w21t25;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
     * -YA
     * todo:
     *  -Usermangaer can make new user doc in DB(Done)
     *  -Usermanager can delete existing doc in DB(Done)
     *  -Usermanager can summon user doc in DB():
     *      -get user name()
     *      -get user email()
     *      -get user class()
     * */
    //attaching firebase good comments to come later please bear with me -YA
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //extra attributes to make ur life easier coming soon ^TM:
    /////////////////////////////////////////////////////////////////////////////////////
    //INITIALIZE PROFILE
    //creates userProfile
    //user is only created at the first launch that means that we can initalize it with empty experiment and trial lists
    public void FB_CreateUserProfile(String id,String name,String email,User user){
        Map<String,Object> userprofile  = new HashMap<>();
        userprofile.put("name",name);
        userprofile.put("email",email);
        userprofile.put("user",user);
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
    /////////////////////////////////////////////////////////////////////////////////////
    //UPDATE
    public void FB_UpdateSubscriptions(ArrayList subscriptions,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("subscriptions", subscriptions)
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
    public void FB_UpdateOwnedExperiments(ArrayList subscriptions,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("ownedExperiments", subscriptions)
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
    public void FB_UpdateConductedTrials(ArrayList subscriptions,String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef
                .update("conductedTrials", subscriptions)
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
