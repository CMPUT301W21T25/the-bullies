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

/**
 * This class manages all the calculations for the Plots
 * @author Yalmaz Samadhi Eden 
 * A manager that fetches an experiment's trials and calls the appropriate method to calculate
 * data points for a line graph to return to the PlotActivity
 */
public class PlotManager {

    ArrayList<Trial> trialArrayList;
    String type;
    SummaryCalculator calculator = new SummaryCalculator();
    LineDataSet lineDataSet;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TrialManager trialManager = new TrialManager();

    /**
     * This method updates the line graph views
     * @param exp experiment being graphed
     * @param lineChart line chart being graphed onto
     */
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
     * @param trials a List of Trial objects whose data will be extracted to generate plot points
     *               for a line graph
     * @return ArrayList<Entry> an ArrayList of plot points to generate the line graph
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Entry> meanByDay(List<Trial> trials) {

        //For each plot point of the graph, the results of the trials up to and including that day
        //will be considered, represented by the ArrayList upToDate which is updated and passed to
        //calculateMean(), so for each day trials were conducted, the graph is representing the mean
        //(the same is true of the following methods, but the data associated with the day is mean,
        //count, and success rate respectively)
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
            //The while loop adds each trial that was conducted on the relevant date to add to
            //upToDate to determine the day's associated data
            while (formattedCurrentDate.equals(formattedPlotDate)) {
                upToDate.add(trial.getResult());
                currentPosition += 1;
                if (currentPosition < trials.size()) {
                    trial = (MeasurableTrial) trials.get(currentPosition);
                    currentDate = trials.get(currentPosition).getDate();
                    formattedCurrentDate = formatDate(currentDate);
                }
                else break;
            }
            //The data is added to plotValues then checks for another date trials were conducted,
            //and repeats the process if it hasn't reached the end of the trials
            plotValues.add(new Entry(dataPoint, calculator.calculateMean(upToDate)));
            dataPoint += 1;
            if (currentPosition < trials.size()) {
                plotPointDate = trials.get(currentPosition).getDate();
                formattedPlotDate = formatDate(plotPointDate);
            }
        }

        return plotValues;
    }

    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the sum of each trial's count up to and including that day
     * @param trials a List of Trial objects whose data will be extracted to generate plot points
     *               for a line graph
     * @return ArrayList<Entry> an ArrayList of plot points to generate the line graph
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
                if (currentPosition < trials.size()) {
                    trial = (MeasurableTrial) trials.get(currentPosition);
                    currentDate = trials.get(currentPosition).getDate();
                    formattedCurrentDate = formatDate(currentDate);
                }
                else break;
            }
            plotValues.add(new Entry(dataPoint, upToDate));
            dataPoint += 1;
            if (currentPosition < trials.size()) {
                plotPointDate = trials.get(currentPosition).getDate();
                formattedPlotDate = formatDate(plotPointDate);
            }
        }

        return plotValues;
    }

    /**
     * Generates an ArrayList of Entry objects to plot on a line graph
     * The first field in new Entry(a, b) is a number that is incremented to represent a change
     * in date, an the second field is the success rate of the trials up to and including that day
     * @param trials a List of Trial objects whose data will be extracted to generate plot points
     *               for a line graph
     * @return ArrayList<Entry> an ArrayList of plot points to generate the line graph
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
                if (currentPosition < trials.size()) {
                    trial = (NonMeasurableTrial) trials.get(currentPosition);
                    currentDate = trials.get(currentPosition).getDate();
                    formattedCurrentDate = formatDate(currentDate);
                }
                else break;
            }
            plotValues.add(new Entry(dataPoint, (float) calculator.calculateSuccessRate(upToDate)));
            dataPoint += 1;
            if (currentPosition < trials.size()) {
                plotPointDate = trials.get(currentPosition).getDate();
                formattedPlotDate = formatDate(plotPointDate);
            }
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

