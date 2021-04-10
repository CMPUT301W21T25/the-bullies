package com.example.cmput301w21t25.managers;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import static java.lang.Math.abs;

import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.activities_experiments.ExperimentDataActivity;
import com.example.cmput301w21t25.activities_graphs.HistogramActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class manages all the calculations for the bargraphs
 */
public class HistogramManager {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();
    private int binCount = 8;
    private ArrayList<BarEntry> entries = new ArrayList<>();;
    private ArrayList<String> xAxisValues = new ArrayList<>();
    private SummaryCalculator calculator = new SummaryCalculator();

    /**
     * This method updates the bar graph view as new data is being added/as the graph is being
     * generated
     * @param context the activity where the
     * @param exp the experiment which is being graphed
     * @param barChart the barChart to be drawn ok
     */
    public void FB_UpdateSummaryViews(Context context, Experiment exp, BarChart barChart){
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
                        if(list.size()%binCount != 0){
                            barChart.setNoDataText("Number of Trials not / by 8");
                            Paint paint =  barChart.getPaint(BarChart.PAINT_INFO);
                            paint.setTextSize(40f);
                            barChart.invalidate();
                        }
                        else{
                            sortBinsMes(list, binCount);
                            BarDataSet barDataSet = new BarDataSet(entries, "Values");
                            BarData barData = new BarData(barDataSet);
                            barChart.setData(barData);

                        }
                    }
                    else{
                        barChart.setNoDataText("No trails to graph");
                        Paint paint =  barChart.getPaint(BarChart.PAINT_INFO);
                        paint.setTextSize(40f);
                    }
                    barChart.invalidate();
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
                        BarDataSet barDataSet = new BarDataSet(entries, "Values");
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);
                    }
                    else{
                        Paint paint =  barChart.getPaint(BarChart.PAINT_INFO);
                        paint.setTextSize(40f);
                    }
                    barChart.invalidate();
                }
            });
        }
    }

    /**
     * This method sorts the measurement, count and non-negative trials into bins to be displayed on
     * the graph
     * @param list list of trials (float)
     * @param binCount number of bins to be divided by. This is hard coded
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBinsMes( ArrayList<Float> list, int binCount){
        //bin count hard coded for now, will be changed
        int bin_width = Math.floorDiv(list.size(),binCount);
        float bin_amount = 0;

        int listSize = list.size();

        //--------------------Histogram  stuff-------------------------------------
        /*int minNum = list.indexOf(Collections.min(list));
        int maxNum = list.indexOf(Collections.max(list));
        double IQR = calculator.calculateUpperQuart(list) - calculator.calculateLowerQuart(list);
        double bin_width = 2 * IQR * Math.pow(listSize, -1/3);

        if (bin_width > maxNum){
            //bin count hard coded for smaller values
            if
            }

        else if(bin_width < maxNum) {
            binCount = (int) ((maxNum - minNum) / bin_width);
        }

        float bin_amount = 0;*/
        //--------------------Histogram stuff END-------------------------------------

        //sort the list in ascending order for easy dividing
        Collections.sort(list);

        //add the values as BarEntries into the designated bins
        int j = 0;
        for (int i = 1; i <= binCount; i++) {
            for(; j < bin_width * i; j++) {
                bin_amount = bin_amount + (float) list.get(j);
            }
            entries.add(new BarEntry(i-1, bin_amount));
            bin_amount = 0;
        }


    }

    /**
     * This method sorts the binomial trials into 2 bins (success and fail) to be shown on a graph
     * @param list list of binomial (boolean) trials
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortBinsBinom(ArrayList<Boolean> list) {
        int trueBin = 0;
        int falseBin = 0;

        //count number of success and failure bins
        for(int i = 0; i < list.size();i++){
            if( list.get(i).equals(true)){
                trueBin = trueBin + 1;
                xAxisValues.add(String.valueOf(trueBin));
            }
            else{
                falseBin = falseBin + 1;
                xAxisValues.add(String.valueOf(falseBin));
            }

        }

        //add the entries into the graph
        entries.add(new BarEntry(0, trueBin));
        entries.add(new BarEntry(1, falseBin));
    }
}
