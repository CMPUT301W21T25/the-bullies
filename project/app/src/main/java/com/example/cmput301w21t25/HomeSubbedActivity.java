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

public class HomeSubbedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        FB_FetchSubscriptions(userID);
        finish();
    }
    /********************************************
     *            DB Functions HERE             *
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String>subscriptionKeys = new ArrayList<String>();
    ArrayList<String>subsSerachResults = new ArrayList<String>();
    public void FB_FetchSubscriptions(String id){
        subscriptionKeys.clear();
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        subscriptionKeys = (ArrayList<String>) document.getData().get("subscriptions");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + subscriptionKeys);
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
    //right now this searches the search val in both tags and description ill sperate them out if u want
    //this only searches subscribed experiments
    public void FB_SearchSubscriptions(String searchVal,ArrayList<String> subscriptionKeys){
        subsSerachResults.clear();
        for (String key : subscriptionKeys) {
            DocumentReference docRef = db.collection("UserProfile").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()&&(boolean)document.get("published")==true&&(document.get("tags")==searchVal||document.get("description")==searchVal)) {
                            subscriptionKeys.add(document.getId());
                            Log.d("YA-DB: ", "Search Result keys" + subsSerachResults);
                        }
                    } else {
                        Log.d("YA-DB: ", "search failed ", task.getException());
                    }
                }
            });
        }
    }

}
