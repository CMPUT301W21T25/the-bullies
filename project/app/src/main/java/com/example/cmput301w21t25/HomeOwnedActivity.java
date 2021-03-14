package com.example.cmput301w21t25;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HomeOwnedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        FB_FetchOwnedKeys(userID);
        finish();
    }
    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> ownedKeys = new ArrayList<String>();
    ArrayList<Experiment>ownedList = new ArrayList<Experiment>();
    public void FB_FetchOwnedKeys(String id){
        ownedKeys.clear();
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ownedKeys = (ArrayList<String>) document.getData().get("ownedExperiments");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + ownedKeys);
                        FB_FetchOwned(ownedKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches subscribed experiments
    public void FB_FetchOwned(ArrayList<String> ownedKeys){
        ownedList.clear();
        if(ownedKeys.isEmpty()==false){
            for (String key : ownedKeys) {
                DocumentReference docRef = db.collection("Experiments").document(key);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Experiment test = document.toObject(Experiment.class);
                                Log.d("YA-DB: ", "SearchResults " + test.getName());
                                //inside here update the feilds and stuff
                            }
                        } else {
                            Log.d("YA-DB: ", "search failed ", task.getException());
                        }
                    }
                });
            }
        }
    }
}
