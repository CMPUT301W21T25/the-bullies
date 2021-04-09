package com.example.cmput301w21t25.managers;

import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import static java.lang.Math.abs;

import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HistogramManager {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();
    private int binCount = 2;
    private ArrayList<BarEntry> entries = new ArrayList<>();;
    private ArrayList<String> xAxisValues = new ArrayList<>();

    public void FB_UpdateSummaryViews(Experiment exp, BarChart barChart){
        //this.binCount = binCount.getText().
        List<String> types = new ArrayList<String>(){{
            add("count");
            add("measurement");
            add("nonnegative count");
        }};

        //CONDITIONAL SEPERATES MESURABLE AND NON MESURABLE TRIALS
        if(types.contains(exp.getType())){
            trialManager.FB_FetchPublishedMesValues(exp, new FirestoreFloatCallback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCallback(ArrayList<Float> list) {
                    if(list.size()>0){
                        //RUN ALL FLOAT RELATED METHODS HERE
                        //TODO: input bincount for custom bins
                        sortBinsMes(list, binCount);
//                        int minNum = list.indexOf(Collections.min(list));
//                        int maxNum = list.indexOf(Collections.max(list));
                    }
                }
            });
        }
        else{
            trialManager.FB_FetchPublishedBoolValues(exp, new FirestoreBoolCallback() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCallback(ArrayList<Boolean> list) {
                    if(list.size()>0){
                        //RUN ALL BOOLEAN RELATED METHODS HERE
                        sortBinsBinom(list);
                    }
                }
            });
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Values");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
    }

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
    }     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBinsMes(ArrayList<Float> list, int binCount){
        //bin count hard coded for now, will be changed
        int bin_width = Math.floorDiv(list.size(),binCount);

        Log.e("bin_width", String.valueOf(bin_width));
        float bin_amount = 0;

        //sort the list
        Collections.sort(list);
        Log.e("binCount", String.valueOf(binCount));
        Log.e("This is the sorted list", String.valueOf(list));

        if(bin_width == 0){
            //Toast toast =  new Toast();
            //Toast.makeText(, "Please add more trials before making a graph", Toast.LENGTH_SHORT).show();
        }
        else{
            int j = 0;
            for (int i = 1; i <= binCount; i++) {
                Log.e("Loop number i", String.valueOf(i));
                for(; j < bin_width * i; j++) { //to the (bin_width*i)){<-will based the index,
                    Log.e("Loop number j", String.valueOf(j));

                    bin_amount = bin_amount + (float) list.get(j);

                    //add the string names for each bin
                    xAxisValues.add(String.valueOf(bin_amount - (float) list.get(j)) + String.valueOf(bin_amount));

                    Log.e("Bin amount", String.valueOf(bin_amount));
                }
                Log.e("entry amount", String.valueOf(bin_amount));
                entries.add(new BarEntry(i-1, bin_amount));
                bin_amount = 0;
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBinsBinom(ArrayList<Boolean> list) {
        int trueBin = 0;
        int falseBin = 0;

        for(int i = 0; i < list.size();i++){
            if( list.get(i).equals(true)){
                Log.e("valueOflistItem (True)", String.valueOf(list.get(i)));
                trueBin = trueBin + 1;
            }
            else{
                Log.e("valueOflistItem (False)", String.valueOf(list.get(i)));
                falseBin = falseBin + 1;
            }
        }

        Log.e("TrueBin", String.valueOf(trueBin));
        Log.e("FalseBin", String.valueOf(falseBin));
        entries.add(new BarEntry(0, trueBin));
        entries.add(new BarEntry(1, falseBin));
    }
}
