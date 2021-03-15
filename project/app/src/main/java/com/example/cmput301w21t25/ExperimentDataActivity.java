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

public class ExperimentDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String experimentID;
        experimentID = getIntent().getStringExtra("EXP_ID");
        FB_FetchTrialKeys(experimentID);
        finish();
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void FB_FetchTrialKeys(String id){
        DocumentReference docRef = db.collection("UserProfile").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> trialKeys = new ArrayList<String>();
                        trialKeys = (ArrayList<String>) document.getData().get("trialKeys");
                        Log.d("YA-DB: ", "DocumentSnapshot data: " + trialKeys);
                        //use this to fix adapter
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
}
