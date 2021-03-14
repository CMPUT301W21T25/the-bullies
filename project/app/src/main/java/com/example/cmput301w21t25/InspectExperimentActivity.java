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

public class InspectExperimentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_subbed);
        String experimentID;
        experimentID = getIntent().getStringExtra("EXP_ID");
        FB_FetchSubscriptionsKeys(experimentID);
        finish();
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void FB_FetchSubscriptionsKeys(String id){
        DocumentReference docRef = db.collection("Experiment").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String type = (String)document.getData().get("type");
                        switch (type){
                            case "Binomial":
                                BinomealExperiment binomealExperiment = new BinomealExperiment();
                                binomealExperiment = document.toObject(BinomealExperiment.class);
                                break;
                            case"Count":
                                CountExperiment countExperiment = new CountExperiment();
                                countExperiment = document.toObject(CountExperiment.class);
                                break;
                            case"Measurement":
                                MeasurementExperiment measurementExperiment = new MeasurementExperiment();
                                measurementExperiment = document.toObject(MeasurementExperiment.class);
                                break;
                        }
                    }
                } else {
                    Log.d("YA-DB: ", "get failed with ", task.getException());
                }
            }
        });
    }
}
