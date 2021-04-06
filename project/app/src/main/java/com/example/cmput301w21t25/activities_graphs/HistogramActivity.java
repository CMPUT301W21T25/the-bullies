package com.example.cmput301w21t25.activities_graphs;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.HistogramManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * This class calculates the graphical representation of the data
 */
public class HistogramActivity extends AppCompatActivity {
    BarChart barChart;
    ArrayList<String> dates;
    ArrayList<BarEntry> barEnteries;
    ArrayList<Integer> xAxis = new ArrayList<>();
    ArrayList<Integer> yAxis = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histogram_barchart);

        //TODO: pass in exp (Serializable)
        Bundle expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP");
        String type = exp.getType();
        barChart = findViewById(R.id.barchart);

        Integer Xmin;
        Integer Xmax;
        Integer Ymin;
        Integer Ymax;
        //TODO: get min and max of trials
       // createMesBarGraph(Xmin, Xmax, Ymin, Ymax);
    }

    //Setting x,y axis for measurement trial
    private void createMesBarGraph(Integer Xmin, Integer Xmax, Integer Ymin, Integer Ymax){
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
