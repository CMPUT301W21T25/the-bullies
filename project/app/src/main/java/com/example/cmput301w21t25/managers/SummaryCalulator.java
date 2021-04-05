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
                        float mean = calculateMean(trialList);
                        Log.d("OUTPUT_MEAN", String.valueOf(mean));
                        double median = calculateMedian(trialList);
                        Log.d("OUTPUT_MEDIAN", String.valueOf(median));
                        double sDev = calculateSD(trialList);
                        Log.d("OUTPUT_STANDARD_DEV", String.valueOf(sDev));
                        double lowerQuart = calculateLowerQuart(trialList);
                        Log.d("OUTPUT_LOWER_QUART", String.valueOf(lowerQuart));
                        double upperQuart = calculateUpperQuart(trialList);
                        Log.d("OUTPUT_UPPER_QUART", String.valueOf(upperQuart));
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
    //I totally changed it so I don't think we have to?
    public float calculateMean(ArrayList<Float> trials){
        float total = 0;

        for (int i = 0; i < trials.size(); i++)
        {
            total += trials.get(i);
        }

        if (trials.size() == 0) {
            return 0;
        }

        else return total / (float) trials.size();
    }

    //Eden totally changed this what even was this before
    public double calculateSD (ArrayList<Float> trials)
    {
        double sDev;
        float mean = calculateMean(trials);
        double squareDiff = 0;

        for (int i = 0; i < trials.size(); i++) {
            squareDiff += Math.pow((trials.get(i) - mean), 2);
        }

        if (trials.size() != 0) {
            sDev = Math.sqrt((squareDiff/ (double) trials.size()));//standard deviation
        }
        else { sDev = 0; }

        return sDev;
    }

    //eden's functions
    public double calculateMedian(ArrayList<Float> trials)
    {
        if (trials.size() == 0) return Double.NEGATIVE_INFINITY;
        //Sort the array
        Collections.sort(trials);
        //Check is there's a midpoint
        if (trials.size() % 2 != 0) {
            return (double) trials.get(trials.size() / 2);
        }
        //Else calculate the average of inner indices
        return (double) trials.get((int)(Math.floor(trials.size() / 2) + Math.ceil(trials.size() / 2)) / 2);
    }

    public double calculateLowerQuart(ArrayList<Float> trials) {
        if (trials.size() == 0) {
            return 0;
        }

        else {
            int midpoint = (int) Math.floor(trials.size() / 2);
            double median = calculateMedian((ArrayList<Float>) trials.subList(0, midpoint));
            return median;
        }
    }

    public double calculateUpperQuart(ArrayList<Float> trials) {
        if (trials.size() == 0) {
            return 0;
        }

        else {
            int midpoint = (int) Math.floor(trials.size() / 2);
            double median = calculateMedian((ArrayList<Float>) trials.subList(midpoint + 1, trials.size() - 1));
            return median;
        }
    }

}
