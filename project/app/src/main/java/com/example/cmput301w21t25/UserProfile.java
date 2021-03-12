package com.example.cmput301w21t25;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class UserProfile {
    //things to do:
    //setup a system to initalize DB
    //setup a system to download UserProfile document
    //setup a system to seperate subscription list from that document
    //this class will be used to GET data from database, not add that will be done through the manager
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userName;
    private ArrayList<String> subscriptions;

    public void getUserDocument(String ID){
        DocumentReference docRef = db.collection("UserProfiles").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userName = (String)document.getData().get("User Name");
                        subscriptions = (ArrayList<String>)document.getData().get("Subscriptions");
                        Log.d(TAG,"username: "+ userName);
                        Log.d(TAG,"subs: "+ userName);
                    }
                }
            }
        });
    }

    //call these getters when u need user info. comment the ones u need and ill just keep adding em
    public String getUserName(String ID){
        return userName;
    }
    public ArrayList<String> getSubscriptions(String ID){
        return subscriptions;
    }
}
