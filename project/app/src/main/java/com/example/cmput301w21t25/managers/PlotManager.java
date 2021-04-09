package com.example.cmput301w21t25.managers;

import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.FirestoreBoolCallback;
import com.example.cmput301w21t25.FirestoreFloatCallback;
import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.MeasurableTrial;
import com.example.cmput301w21t25.trials.NonMeasurableTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PlotManager {

    ArrayList<Trial> trialArrayList;
    String type;
    SummaryCalculator calculator = new SummaryCalculator();
    LineDataSet lineDataSet;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();

    public void FB_UpdateSummaryViews(Experiment exp, LineChart lineChart){
        this.type = exp.getType();
        //CONDITIONAL SEPERATES MESURABLE AND NON MESURABLE TRIALS
        trialManager.FB_FetchPublishedTrial(exp, new FirestoreTrialCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCallback(List<Trial> list) {
                if (list.size() > 0) {
                    //choosing which method to use for the line graph
                    if (type.equals("measurement") || type.equals("nonnegative count")) {
                        lineDataSet =
                                new LineDataSet(meanByDay(list), "Mean of all Trials");
                    } else if (type.equals("count")) {
                        lineDataSet =
                                new LineDataSet(countByDay(list), "Count of all Trials");
                    }
                    if (type.equals("binomial")) {
                        lineDataSet =
                                new LineDataSet(successRateByDay(list), "Success rate of all Trials");
                    }

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet);

                    LineData data = new LineData(dataSets);
                    lineChart.setData(data);
                }
                else{
                    lineChart.setNoDataText("No trials to plot");
                    Paint paint =  lineChart.getPaint(BarChart.PAINT_INFO);
                    paint.setTextSize(40f);
                }
                lineChart.invalidate();
            }
        });
    }


    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the mean of the trials up to and including that day
     * @param trials
     * @return ArrayList<Entry>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> meanByDay(List<Trial> trials) {

        ArrayList<Float> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date plotPointDate = trials.get(0).getDate();
        String formattedPlotDate = formatDate(plotPointDate);
        int currentPosition = 0;
        int dataPoint = 0;

        MeasurableTrial trial = (MeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            Date currentDate = trials.get(currentPosition).getDate();
            String formattedCurrentDate = formatDate(currentDate);
            while (formattedCurrentDate.equals(formattedPlotDate)) {
                upToDate.add(trial.getResult());
                currentPosition += 1;
                trial = (MeasurableTrial) trials.get(currentPosition);
                currentDate = trials.get(currentPosition).getDate();
                formattedCurrentDate = formatDate(currentDate);
            }
            plotValues.add(new Entry(dataPoint, calculator.calculateMean(upToDate)));
            dataPoint += 1;
            plotPointDate = trials.get(currentPosition).getDate();
            formattedPlotDate = formatDate(plotPointDate);
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
    public List<Entry> countByDay(List<Trial> trials) {

        int upToDate = 0;
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date plotPointDate = trials.get(0).getDate();
        String formattedPlotDate = formatDate(plotPointDate);
        int currentPosition = 0;
        int dataPoint = 0;

        MeasurableTrial trial = (MeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            Date currentDate = trials.get(currentPosition).getDate();
            String formattedCurrentDate = formatDate(currentDate);
            while (formattedCurrentDate.equals(formattedPlotDate)) {
                upToDate += trial.getResult();
                currentPosition += 1;
                trial = (MeasurableTrial) trials.get(currentPosition);
                currentDate = trials.get(currentPosition).getDate();
                formattedCurrentDate = formatDate(currentDate);
            }
            plotValues.add(new Entry(dataPoint, upToDate));
            dataPoint += 1;
            plotPointDate = trials.get(currentPosition).getDate();
            formattedPlotDate = formatDate(plotPointDate);
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
    public List<Entry> successRateByDay(List<Trial> trials) {

        ArrayList<Boolean> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date plotPointDate = trials.get(0).getDate();
        String formattedPlotDate = formatDate(plotPointDate);
        int currentPosition = 0;
        int dataPoint = 0;

        NonMeasurableTrial trial = (NonMeasurableTrial) trials.get(currentPosition);

        while (currentPosition < trials.size()) {
            Date currentDate = trials.get(currentPosition).getDate();
            String formattedCurrentDate = formatDate(currentDate);
            while (formattedCurrentDate.equals(formattedPlotDate)) {
                upToDate.add(trial.getResult());
                currentPosition += 1;
                trial = (NonMeasurableTrial) trials.get(currentPosition);
                currentDate = trials.get(currentPosition).getDate();
                formattedCurrentDate = formatDate(currentDate);
            }
            plotValues.add(new Entry(dataPoint, (float) calculator.calculateSuccessRate(upToDate)));
            dataPoint += 1;
            plotPointDate = trials.get(currentPosition).getDate();
            formattedPlotDate = formatDate(plotPointDate);
        }

        return plotValues;
    }

    /**
     *
     * @param date
     * The date the comment was created
     * @return
     * A formatted version of the date (String)
     */
    private String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

}

/*
 //Samadhi's version, just in case mine is broken after fixing :')
 @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Entry> successRateByDay(List<Trial> trials) {

        ArrayList<Boolean> upToDate = new ArrayList<>();
        ArrayList<Entry> plotValues = new ArrayList<>();
        trials.sort(Comparator.comparing(Trial::getDate));

        Date currentDate = trials.get(0).getDate();
        int currentPosition = 0;
        int dataPoint = 0;

        NonMeasurableTrial trial = (NonMeasurableTrial) trials.get(currentPosition);

        Log.e("trials.size()", String.valueOf(trials.size()));
        while (currentPosition < trials.size()) {
            currentDate = trials.get(currentPosition).getDate();
            //while (trial.getDate().getDate() == currentDate.getDate() && trial.getDate().getMonth() == currentDate.getMonth() && trial.getDate().getYear() == currentDate.getYear()) {
                upToDate.add(trial.getResult());
                Log.e("currentPostion", String.valueOf(currentPosition));
                trial = (NonMeasurableTrial) trials.get(currentPosition);
            //}
            currentPosition = currentPosition + 1;
            Log.e("currentPostion2", String.valueOf(currentPosition));
            plotValues.add(new Entry(dataPoint, (float) calculator.calculateSuccessRate(upToDate)));
            dataPoint += 1;
        }

        return plotValues;
    }
 */
