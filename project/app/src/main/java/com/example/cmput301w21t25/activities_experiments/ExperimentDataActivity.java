package com.example.cmput301w21t25.activities_experiments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.sqrt;

public class ExperimentDataActivity extends AppCompatActivity {
    Toolbar experimentInfo;

    TextView descriptionTextView;
    TextView minimumTrialsTextView;
    TextView currentTrialsTextView;
    TextView LquartilesTextView;
    TextView UquartilesTextView;
    TextView medianTextView;
    TextView meanTextView;
    TextView deviationTextView;
    TextView successRateTextView;

    public double mean;
    private double sDev;
    private double median;
    private double Lquart;
    private double Uquart;
    private double successRate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_experiment_data);
        String type;
        Experiment exp = (Experiment) getIntent().getSerializableExtra("EXP");
        type = exp.getType();
        Log.d("WHAT_IS_TYPE", type);

        experimentInfo = findViewById(R.id.viewExperimentDataInfo);
        descriptionTextView = findViewById(R.id.viewExperimentDataDescription);
        minimumTrialsTextView = findViewById(R.id.minimumTrials);
        currentTrialsTextView = findViewById(R.id.conductedTrials);

        LquartilesTextView = findViewById(R.id.Lquartiles);
        UquartilesTextView = findViewById(R.id.Uquartiles);
        medianTextView = findViewById(R.id.median);
        meanTextView = findViewById(R.id.mean);
        deviationTextView = findViewById(R.id.stDev);
        successRateTextView = findViewById(R.id.successRate);

        switch(type){
            case "count":
                CountExperiment countParent = (CountExperiment) exp;
                FB_FetchSummary(countParent);
                break;
            case "binomial":
                BinomialExperiment binomialParent = (BinomialExperiment) exp;
                FB_FetchSummary(binomialParent);
                break;
            case "non-neg-count":
                NonNegCountExperiment nnCountParent = (NonNegCountExperiment) exp;
                FB_FetchSummary(nnCountParent);
                break;
            case "measurement":
                MeasurementExperiment measurementParent = (MeasurementExperiment) exp;
                FB_FetchSummary(measurementParent);
                break;
        }

        experimentInfo.setTitle(exp.getName());
        experimentInfo.setSubtitle(formatDate(exp.getDate()));
        descriptionTextView.setText(exp.getDescription());

        minimumTrialsTextView.setText("Minimum Number of Trials: " + Integer.toString(exp.getMinNumTrials()));
        currentTrialsTextView.setText("Current Number of Trials: " + Integer.toString(exp.getTrialKeys().size()));
        //finish();
    }

    private String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer countSUM = 0;
    public void FB_FetchSummary(CountExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        ArrayList<Integer> values = new ArrayList<Integer>();
        CollectionReference docRef = db.collection("TrialDocs");
        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(keys.contains(document.getId())){
                            CountTrial trial = document.toObject(CountTrial.class);
                            values.add(trial.getResult());
                            countSUM = countSUM + trial.getResult();//total
                        }
                    }
                    mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff =0;
                    double total = 0;
                    for(int i =0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    if (total != 0) {
                        sDev = sqrt((squareDiff/total));//standard deviation
                    }
                    else sDev = 0;
                    median = values.get(values.size()/2);
                    Lquart = values.get(values.size()/4);
                    Uquart = values.get(3*values.size()/4);
                    showNonBinomialStats();
                }
            }
        });
    }
    public void FB_FetchSummary(NonNegCountExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        ArrayList<Integer> values = new ArrayList<Integer>();
        CollectionReference docRef = db.collection("TrialDocs");
        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(keys.contains(document.getId())){
                            CountTrial trial = document.toObject(CountTrial.class);
                            values.add(trial.getResult());
                            countSUM = countSUM + trial.getResult();//total
                        }
                    }
                    mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff =0;
                    double total = 0;
                    for(int i =0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    if (total != 0) {
                        sDev = sqrt((squareDiff/total));//standard deviation
                    }
                    else sDev = 0;
                    median = values.get(values.size()/2);
                    Lquart = values.get(values.size()/4);
                    Uquart = values.get(3*values.size()/4);
                    showNonBinomialStats();
                }
            }
        });
    }
    private Float countSUMF;
    public void FB_FetchSummary(MeasurementExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        ArrayList<Float> values = new ArrayList<Float>();
        CollectionReference docRef = db.collection("TrialDocs");
        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(keys.contains(document.getId())){
                            MeasurementTrial trial = document.toObject(MeasurementTrial.class);
                            values.add(trial.getResult());
                            countSUMF = countSUMF + trial.getResult();//total
                        }
                    }
                    mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff = 0;
                    double total = 0;
                    for(int i = 0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    if (total != 0) {
                        sDev = sqrt((squareDiff/total));//standard deviation
                    }
                    else sDev = 0;
                    median = values.get(values.size()/2);
                    Lquart = values.get(values.size()/4);
                    Uquart = values.get(3*values.size()/4);
                    showNonBinomialStats();
                }
            }
        });
    }
    private Integer successCount = 0;
    private Integer totalCount = 0;
    public void FB_FetchSummary(BinomialExperiment parent){
        ArrayList<String>keys = parent.getTrialKeys();
        ArrayList<Boolean> values = new ArrayList<Boolean>();
        CollectionReference docRef = db.collection("TrialDocs");
        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(keys.contains(document.getId())){
                            BinomialTrial trial = document.toObject(BinomialTrial.class);
                            values.add(trial.getResult());
                            if (trial.getResult()==true){
                                successCount++;//true
                            }
                            totalCount++;//total
                        }
                    }
                    if (totalCount != 0) {
                        successRate = Double.valueOf(((float) successCount / (float) totalCount));
                    }
                    else successRate = 0;
                    showBinomialStats();
                }
            }
        });
    }

    public void showBinomialStats() {
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            medianTextView.setText("Median: N/A");
            meanTextView.setText("Mean: N/A");
            deviationTextView.setText("Standard Deviation: N/A");
            LquartilesTextView.setText("LQuartiles: N/A");
            UquartilesTextView.setText("UQuartiles: N/A");
            successRateTextView.setText("Success Rate: " + decimalFormat.format(successRate));
        }

    public void showNonBinomialStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        medianTextView.setText("Median: " + decimalFormat.format(median));
        meanTextView.setText("Mean: " + decimalFormat.format(mean));
        deviationTextView.setText("Standard Deviation: " + decimalFormat.format(sDev));
        LquartilesTextView.setText("LQuartiles: " + decimalFormat.format(Lquart));
        UquartilesTextView.setText("UQuartiles: " + decimalFormat.format(Uquart));
        successRateTextView.setText("Success Rate: " + decimalFormat.format(successRate));
    }

}
