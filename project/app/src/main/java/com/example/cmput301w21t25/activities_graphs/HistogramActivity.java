package com.example.cmput301w21t25.activities_graphs;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.HistogramManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * This class calculates the graphical representation of the data
 */
public class HistogramActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<String> dates;
    private ArrayList<BarEntry> barEnteries;
    private ArrayList<Integer> xAxis = new ArrayList<>();
    private ArrayList<Integer> yAxis = new ArrayList<>();
    private HistogramManager histogramManager = new HistogramManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        Bundle expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP");
        String type = exp.getType();

        setTitle(exp.getName());
        barChart = findViewById(R.id.barchart);


        Integer Xmin;
        Integer Xmax;
        Integer Ymin;
        Integer Ymax;
        //TODO: get min and max of trials
       // createBarGraph(Xmin, Xmax, Ymin, Ymax);
        histogramManager.FB_UpdateSummaryViews(exp);

    }

    //Setting x,y axis for measurement trial
    private void createBarGraph(Integer Xmin, Integer Xmax, Integer Ymin, Integer Ymax){
        xAxis = getNumList(Xmin, Xmax);
        yAxis = getNumList(Ymin, Ymax);
    }


    private ArrayList<Integer> getNumList(Integer min, Integer max){
        ArrayList<Integer> list = new ArrayList<>();
        while(min.compareTo(max) <=0){
            list.add(min);
            min = min +1;
        }
        return list;
    }

}
