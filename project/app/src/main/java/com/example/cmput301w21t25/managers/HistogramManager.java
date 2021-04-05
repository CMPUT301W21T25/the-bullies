package com.example.cmput301w21t25.managers;

import android.util.Log;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HistogramManager {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();
    public void FB_UpdateSummaryViews(Experiment exp){
        List<String> types = new ArrayList<String>(){{
            add("count");
            add("measurement");
            add("nonnegative count");
        }};
        //CONDITIONAL SEPERATES MESURABLE AND NON MESURABLE TRIALS
        if(types.contains(exp.getType())){
            trialManager.FB_FetchPublishedMesValues(exp, new FirestoreFloatCallback() {
                @Override
                public void onCallback(ArrayList<Float> list) {
                    if(list.size()>0){
                        //RUN ALL FLOAT RELATED METHODS HERE
                    }
                }
            });
        }
        else{
            trialManager.FB_FetchPublishedBoolValues(exp, new FirestoreBoolCallback() {
                @Override
                public void onCallback(ArrayList<Boolean> list) {
                    //RUN ALL BOOLEAN RELATED METHODS HERE
                }
            });
        }
    }
}
