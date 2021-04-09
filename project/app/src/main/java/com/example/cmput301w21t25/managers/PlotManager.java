package com.example.cmput301w21t25.managers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlotManager {

    ArrayList<Trial> trialArrayList;

    private ArrayList<Trial> FB_ReturnTrials(){

        return trialArrayList;
    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();
    public void FB_UpdateSummaryViews(Experiment exp, BarChart barChart){
        List<String> types = new ArrayList<String>(){{
            add("count");
            add("measurement");
            add("nonnegative count");
        }};
        //CONDITIONAL SEPERATES MESURABLE AND NON MESURABLE TRIALS
        if(types.contains(exp.getType())){
            trialManager.FB_FetchPublishedTrial(exp, new FirestoreTrialCallback() {
                @Override
                public void onCallback(List<Trial> list) {
                    if(list.size()>0){
                        //IF HERE
                    }
                }
            });
        }
    }
}
