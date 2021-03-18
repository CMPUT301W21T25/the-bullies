package com.example.cmput301w21t25.activities_experiments;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
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
        String type;
        type = getIntent().getStringExtra("TYPE");
        switch(type){
            case "count":
                CountExperiment countParent = (CountExperiment) getIntent().getSerializableExtra("EXP");
                FB_FetchSummary(countParent);
                break;
            case "binomial":
                BinomialExperiment binomialParent = (BinomialExperiment) getIntent().getSerializableExtra("EXP");
                //FB_FetchSummary(binomialParent);
                break;
            case "non-neg-count":
                NonNegCountExperiment nnCountParent = (NonNegCountExperiment) getIntent().getSerializableExtra("EXP");
                //FB_FetchSummary(nnCountParent);
                break;
            case "measurement":
                MeasurementExperiment measurementParent = (MeasurementExperiment) getIntent().getSerializableExtra("EXP");
                //FB_FetchSummary(measurementParent);
                break;
        }
        //finish();
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Integer countSUM = 0;
    public void FB_FetchSummary(CountExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        for (String key : keys) {
            Log.d("YA-DB: trial", key);
            DocumentReference docRef = db.collection("TrialDocs").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long value = (Long) document.getData().get("result");
                            countSUM = countSUM + value.intValue();
                            Log.d("YA-DB: trial", String.valueOf(countSUM));
                        }
                        else{
                        }
                    } else {
                        Log.d("YA-DB: ", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
    public void FB_FetchSummary(NonNegCountExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        String type = parent.getType();
        for (String key : keys) {
            DocumentReference docRef = db.collection("TrialDocs").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long value = (Long) document.getData().get("result");
                            countSUM = countSUM + value.intValue();
                        }
                    } else {
                        Log.d("YA-DB: ", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
    private Float countSUMF;
    public void FB_FetchSummary(MeasurementExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        String type = parent.getType();
        for (String key : keys) {
            DocumentReference docRef = db.collection("TrialDocs").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Double value = (Double) document.getData().get("result");
                            countSUMF = countSUMF + value.floatValue();
                        }
                    } else {
                        Log.d("YA-DB: ", "get failed with ", task.getException());
                    }
                }
            });
        }
    }
    private Integer successCount = 0;
    public void FB_FetchSummary(BinomialExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        for (String key : keys) {
            DocumentReference docRef = db.collection("TrialDocs").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            if((Boolean) document.getData().get("result")){
                                successCount++;
                                Log.d("YA-DB: ", String.valueOf(successCount));
                            }
                        }
                    } else {
                        Log.d("YA-DB: ", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

}
