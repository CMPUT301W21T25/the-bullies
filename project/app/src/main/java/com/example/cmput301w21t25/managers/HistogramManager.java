package com.example.cmput301w21t25.managers;

import android.util.Log;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HistogramManager {

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
            trialManager.FB_FetchPublishedMesValues(exp, new FirestoreFloatCallback() {
                @Override
                public void onCallback(ArrayList<Float> list) {

                    if(list.size()>0){
                        int minNum = list.indexOf(Collections.min(list));
                        int maxNum = list.indexOf(Collections.max(list));
                        //RUN ALL FLOAT RELATED METHODS HERE
                    }



                }
            });
        }
        else{
            trialManager.FB_FetchPublishedBoolValues(exp, new FirestoreBoolCallback() {
                @Override
                public void onCallback(ArrayList<Boolean> list) {
                    if(list.size()>0){

                        //RUN ALL BOOLEAN RELATED METHODS HERE
                    }

                }
            });
        }
    }
    ///
    /*
    binCOunt = number of bins input by user
    list.sort()
    List<BarEntry> entries = new ArrayList<>();
    bin_width = (list.size/bincount)
    for(i = 1; binCount){<-outside loop runs for the count bins so if theres 3 bins itll run three times
        for(j= (bin_width*(i-1))); to the (bin_width*i)){<-will based the index,
            bin1.add(list[i]);
        }
        entries.add(new BarEntry(3f, 50f));
    }


     */
}
