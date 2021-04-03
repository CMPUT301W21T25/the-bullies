package com.example.cmput301w21t25.managers;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.example.cmput301w21t25.FirestoreStringCallback;
import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SummaryCalulator {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();


    public void FB_UpdateSummaryViews(Experiment exp){
        trialManager.FB_FetchPublishedTrial(exp, new FirestoreTrialCallback() {
            @Override
            public void onCallback(List<Trial> list) {
                if(list.size()>0){
                    for (Trial item:list) {
                        Log.d("YA_TEST:",item.getTrialId());
                    }
                }
            }
        });
    }

    //cite:https://stackoverflow.com/questions/37930631/standard-deviation-of-an-arraylist
    public float calculateMean(ArrayList<Float> list){
        int total = 0;

        for ( int i= 0;i < list.size(); i++)
        {
            float currentNum = list.get(i);
            total+= currentNum;
        }
        return total/list.size();
    }
    public double calculateSD (ArrayList<Float> list)
    {
        double mean= calculateMean(list);
        double temp =0;
        for ( int i= 0; i <list.size(); i++)
        {
            temp= Math.pow(i-mean, 2);
        }
        return Math.sqrt(calculateMean(list));
    }
    //eden's functions
    public Double calculateMedianFloat(ArrayList<Float> arrayList)
    {
        if (arrayList.size() == 0) return Double.NEGATIVE_INFINITY;
        //Sort the array
        Collections.sort(arrayList);
        //Check is there's a midpoint
        if (arrayList.size() % 2 != 0) {
            return (double) arrayList.get(arrayList.size() / 2);
        }
        //Else calculate the average of inner indices
        return (double) arrayList.get((int)(Math.floor(arrayList.size() / 2) + Math.ceil(arrayList.size() / 2)) / 2);
    }
}
