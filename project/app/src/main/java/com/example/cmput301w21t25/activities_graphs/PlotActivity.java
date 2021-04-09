package com.example.cmput301w21t25.activities_graphs;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.managers.SummaryCalculator;
import com.example.cmput301w21t25.trials.MeasurableTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PlotActivity extends AppCompatActivity {

    SummaryCalculator calculator = new SummaryCalculator();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> medianByDay(ArrayList<Trial> trials) {

        ArrayList<Float> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(trial -> trial.getDate()));

        Date currentDate = trials.get(0).getDate();
        Integer currentPosition = 0;
        Integer dataPoint = 0;

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
}

