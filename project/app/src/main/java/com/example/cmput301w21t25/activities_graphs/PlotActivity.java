package com.example.cmput301w21t25.activities_graphs;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.managers.PlotManager;
import com.example.cmput301w21t25.managers.SummaryCalculator;
import com.example.cmput301w21t25.trials.MeasurableTrial;
import com.example.cmput301w21t25.trials.NonMeasurableTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PlotActivity extends AppCompatActivity {

    //If you are generating a line graph for a non-negative or measurement experiment, generate the
    //list of plot points using meanByDay()
    //For count trials, countByDay()
    //For binomial trials, successRateByDay

    String type;
    ArrayList<Trial> trials;
    String userRequest;

    LineChart lineChart;
    LineDataSet lineDataSet;
    SummaryCalculator calculator = new SummaryCalculator();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        type = exp.type();

        //trials = PlotManager.FB_

        //choosing which method to use for the line graph
        lineChart = (LineChart) findViewById(R.id.line_chart);
        if(type.equals("measurement") || type.equals("nonnegative count")){
            lineDataSet =
                    new LineDataSet(meanByDay(trials), "Mean of all Trials");
        }
        else if(type.equals("count")){
            lineDataSet =
                    new LineDataSet(countByDay(trials), "Count of all Trials");
        }
        else if(type.equals("binomial")){
            lineDataSet =
                    new LineDataSet(successRateByDay(trials), "Success rate of all Trials");
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }


    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the mean of the trials up to and including that day
     * @param trials
     * @return ArrayList<Entry>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> meanByDay(ArrayList<Trial> trials) {

        ArrayList<Float> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date currentDate = trials.get(0).getDate();
        int currentPosition = 0;
        int dataPoint = 0;

        MeasurableTrial trial = (MeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            while (trial.getDate().getDate() == currentDate.getDate() && trial.getDate().getMonth() == currentDate.getMonth() && trial.getDate().getYear() == currentDate.getYear()) {
                upToDate.add(trial.getResult());
                currentPosition += 1;
                trial = (MeasurableTrial) trials.get(currentPosition);
            }
            plotValues.add(new Entry(dataPoint, calculator.calculateMean(upToDate)));
            dataPoint += 1;
            currentDate = trials.get(currentPosition + 1).getDate();
        }

    return plotValues;
    }

    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the sum of each trial's count up to and including that day
     * @param trials
     * @return ArrayList<Entry>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> countByDay(ArrayList<Trial> trials) {

        int upToDate = 0;
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date currentDate = trials.get(0).getDate();
        int currentPosition = 0;
        int dataPoint = 0;

        MeasurableTrial trial = (MeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            while (trial.getDate().getDate() == currentDate.getDate() && trial.getDate().getMonth() == currentDate.getMonth() && trial.getDate().getYear() == currentDate.getYear()) {
                upToDate += trial.getResult();
                currentPosition += 1;
                trial = (MeasurableTrial) trials.get(currentPosition);
            }
            plotValues.add(new Entry(dataPoint, upToDate));
            dataPoint += 1;
            currentDate = trials.get(currentPosition + 1).getDate();
        }

        return plotValues;
    }

    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the success rate of the trials up to and including that day
     * @param trials
     * @return ArrayList<Entry>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> successRateByDay(ArrayList<Trial> trials) {

        ArrayList<Boolean> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date currentDate = trials.get(0).getDate();
        int currentPosition = 0;
        int dataPoint = 0;

        NonMeasurableTrial trial = (NonMeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            while (trial.getDate().getDate() == currentDate.getDate() && trial.getDate().getMonth() == currentDate.getMonth() && trial.getDate().getYear() == currentDate.getYear()) {
                upToDate.add(trial.getResult());
                currentPosition += 1;
                trial = (NonMeasurableTrial) trials.get(currentPosition);
            }
            plotValues.add(new Entry(dataPoint, (float) calculator.calculateSuccessRate(upToDate)));
            dataPoint += 1;
            currentDate = trials.get(currentPosition + 1).getDate();
        }

        return plotValues;
    }
}

