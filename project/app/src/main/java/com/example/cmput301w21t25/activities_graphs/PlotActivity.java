package com.example.cmput301w21t25.activities_graphs;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.managers.SummaryCalculator;
import com.example.cmput301w21t25.trials.MeasurableTrial;
import com.example.cmput301w21t25.trials.NonMeasurableTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Eden
 */
public class PlotActivity extends AppCompatActivity {

    //If you are generating a line graph for a non-negative or measurement experiment, generate the
    //list of plot points using meanByDay()
    //For count trials, countByDay()
    //For binomial trials, successRateByDay

    SummaryCalculator calculator = new SummaryCalculator();

    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the mean of the trials up to and including that day
     * @param trials an array list of all trials that are published and non-hidden for a particular
     *               experiment
     * @return ArrayList<Entry> plotValues: the points to be plotted on the line graph
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
     * @param trials an array list of all trials that are published and non-hidden for a particular
     *          experiment
     * @return ArrayList<Entry> plotValues: the points to be plotted on the line graph
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
     * @param trials an array list of all trials that are published and non-hidden for a particular
     *               experiment
     * @return ArrayList<Entry> plotValues: the points to be plotted on the line graph
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

