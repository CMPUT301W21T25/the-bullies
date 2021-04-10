package com.example.cmput301w21t25.managers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.FirestoreStringCallback;
import com.example.cmput301w21t25.activities_main.MainActivity;
import com.example.cmput301w21t25.activities_user.GenerateUserActivity;
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
import com.google.firebase.firestore.Query;
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
     * this ensures that the user name is unique
     * @param name this is the string value the function will check the uniqueness of (the username)
     * @return
     */
    public void FB_isUnique(String name, String userID, String email, Context context, String mode) {
        Query query = db.collection("UserProfile").whereEqualTo("name", name);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String id = "No results";
                    String nameOnDB = "No results";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //this either finds one or 0 docs --UPDATE, if name is "" then it will find a bunch of docs
                        id = document.getId(); //represents the id of a user with the same name as you
                    }
                    //case 1: You are creating a new user
                    if (mode.equals("create") && (id.equals("No results") || name.equals(""))) { //creating and name is unique or ""
                        //safe to create new user!
                        User user = new User(name, email);
                        FB_CreateUserProfile(userID, name, email, user);
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Profile Created!", Toast.LENGTH_SHORT).show();
                    }
                    //case 1.5: You are skipping the user creation
                    else if (mode.equals("skip")) { //make blank user
                        //safe to create new user!
                        User user = new User(name, email);
                        FB_CreateUserProfile(userID, "", "", user);
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        Toast.makeText(context, "Skipping User Creation", Toast.LENGTH_SHORT).show();
                    }
                    //case 2: You are updating your email only
                    else if (mode.equals("update") && (userID.equals(id) || name.equals(""))) { //updating and name is the same as your old one, or ""
                        //the name is the same as your old name
                        FB_UpdateName(name, userID);
                        FB_UpdateEmail(email, userID);
                        ExperimentManager experimentManager = new ExperimentManager();
                        experimentManager.FB_UpdateName(userID,name);
                        Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                    //case 3: You are updating your name
                    else if (mode.equals("update") && (id.equals("No results") || name.equals(""))) { //nobody has the same name as you
                        //the name is not taken
                        FB_UpdateName(name, userID);
                        FB_UpdateEmail(email, userID);
                        ExperimentManager experimentManager = new ExperimentManager();
                        experimentManager.FB_UpdateName(userID,name);
                        Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context, "This username is already taken!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("curtis", "Error getting documents: ", task.getException());
                }
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
     *
     * @param id
     * @param fsCallback
     */
    //this function is a test function to pull the keys synchronously, they dont but were close enough
    public void FB_FetchOwnedExperimentKeys(String id, FirestoreStringCallback fsCallback){//the fsCallback is an object that functions similarly to a wait function
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
    public void FB_FetchSubbedExperimentKeys(String id, FirestoreStringCallback fsCallback){//the fsCallback is an object that functions similarly to a wait function
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
    public void FB_FetchUserInfo(String id, FirestoreStringCallback fsCallback) {
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

    /**
     *
     * @param experiment
     * @param userAdapter
     * @param users
     */
    public void FB_FetchContributors(Experiment experiment, ArrayAdapter<User> userAdapter, ArrayList<User> users) {
        ExperimentManager experimentManager = new ExperimentManager();
        db.collection("UserProfile")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        experimentManager.FB_FetchContribKeys(experiment.getFb_id(), new FirestoreStringCallback() {
                            @Override
                            public void onCallback(ArrayList<String> list) {
                                users.clear();
                                userAdapter.notifyDataSetChanged();
                                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                {
                                    if (list.contains(doc.getId())) {
                                        User temp = doc.toObject(User.class);
                                        temp.setUserID(doc.getId());
                                        users.add(temp);
                                        userAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        });
                    }
                });
//        db.collection("UserProfile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                users.clear();
//                userAdapter.notifyDataSetChanged();
//                for(QueryDocumentSnapshot doc: task.getResult())
//                {
//                    if (experiment.getContributorUsersKeys().contains(doc.getId())) {
//                        User temp = doc.toObject(User.class);
//                        temp.setUserID(doc.getId());
//                        users.add(temp);
//                        userAdapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
    }
    /**
     *
     * @param experiment
     * @param userAdapter
     * @param users
     */
    public void FB_FetchHidden(Experiment experiment, ArrayAdapter<User> userAdapter, ArrayList<User> users) {
        ExperimentManager experimentManager = new ExperimentManager();
        db.collection("UserProfile")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        experimentManager.FB_FetchHiddenKeys(experiment.getFb_id(), new FirestoreStringCallback() {
                            @Override
                            public void onCallback(ArrayList<String> list) {
                                users.clear();
                                userAdapter.notifyDataSetChanged();
                                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                {
                                    if (list.contains(doc.getId())) {
                                        User temp = doc.toObject(User.class);
                                        temp.setUserID(doc.getId());
                                        users.add(temp);
                                        userAdapter.notifyDataSetChanged();
                                        Log.d("TSTHIDDEN: ", temp.getUserID());
                                    }
                                }
                            }
                        });
                    }
                });
    }
    /**
     * End of database stuff -YA
     * */
}
