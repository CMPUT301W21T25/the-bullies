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


    public void FB_UpdateSummaryViewsForMeasurable(Experiment exp){
        trialManager.FB_FetchPublishedTrial(exp, new FirestoreTrialCallback() {
            @Override
            public void onCallback(List<Trial> list) {
                //EDEN LOOK HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                if(list.size()>0){
                    List<String> types = new ArrayList<String>(){{
                        add("count");
                        add("measurement");
                        add("nonnegative count");
                    }};
                    //conditional so binomial's get treated seperatly
                    if(types.contains(exp.getType())){
                        //this loop is just used for testing u can delete it later
                        for (Trial item:list) {
                            Log.d("YA_TEST:",item.getTrialId());//<----this prints out the trial lists
                        }
                        //insert method calls here
                        //ex: float mean = mean(list)<--------------------------list is an arraylist of trials that u will use
                        //Log.d("OUTPUTS:", String.valueOf(mean))<<-------------GOAL IS TO MAKE A LOG FOR EACH VALUE U WANNA SHOW IE: MEAN,SD,ETC
                    }
                    else{
                        //this loop is just used for testing u can delete it later
                        for (Trial item:list) {
                            Log.d("YA_TEST:",item.getTrialId());//<----this prints out the trial lists
                        }
                        //insert method calls here
                        //ex: float mean = mean(list)<--------------------------list is an arraylist of trials that u will
                        //Log.d("OUTPUTS:", String.valueOf(mean))<<-------------GOAL IS TO MAKE A LOG FOR EACH VALUE U WANNA SHOW IE: MEAN,SD,ETC
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
