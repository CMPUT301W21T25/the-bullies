package com.example.cmput301w21t25.activities_experiments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Math.sqrt;

public class ExperimentDataActivity extends AppCompatActivity {
    Toolbar experimentInfo;

    TextView descriptionTextView;
    TextView minimumTrialsTextView;
    TextView currentTrialsTextView;
    TextView quartilesTextView;
    TextView medianTextView;
    TextView meanTextView;
    TextView deviationTextView;
    TextView successRateTextView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_experiment_data);
        String type;
        type = getIntent().getStringExtra("TYPE");

        experimentInfo = findViewById(R.id.viewExperimentDataInfo);
        descriptionTextView = findViewById(R.id.viewExperimentDataDescription);
        minimumTrialsTextView = findViewById(R.id.minimumTrials);
        currentTrialsTextView = findViewById(R.id.conductedTrials);
        quartilesTextView = findViewById(R.id.quartiles);
        medianTextView = findViewById(R.id.median);
        meanTextView = findViewById(R.id.mean);
        deviationTextView = findViewById(R.id.stDev);
        successRateTextView = findViewById(R.id.successRate);

        switch(type){
            case "count":
                CountExperiment countParent = (CountExperiment) getIntent().getSerializableExtra("EXP");
                displayExperimentInfo(countParent);
                FB_FetchSummary(countParent);
                break;
            case "binomial":
                BinomialExperiment binomialParent = (BinomialExperiment) getIntent().getSerializableExtra("EXP");
                displayExperimentInfo(binomialParent);
                FB_FetchSummary(binomialParent);
                break;
            case "non-neg-count":
                NonNegCountExperiment nnCountParent = (NonNegCountExperiment) getIntent().getSerializableExtra("EXP");
                displayExperimentInfo(nnCountParent);
                FB_FetchSummary(nnCountParent);
                break;
            case "measurement":
                MeasurementExperiment measurementParent = (MeasurementExperiment) getIntent().getSerializableExtra("EXP");
                displayExperimentInfo(measurementParent);
                FB_FetchSummary(measurementParent);
                break;
        }

        //finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void displayExperimentInfo(Experiment experiment) {
        experimentInfo.setTitle(experiment.getName());
        experimentInfo.setSubtitle(formatDate(experiment.getDate()));
        descriptionTextView.setText(experiment.getDescription());
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
                    double mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff =0;
                    double total = 0;
                    double sDev =0.0;
                    for(int i =0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    sDev = sqrt((squareDiff/total));//standard deviation
                    int median = values.get(values.size()/2);
                    int Lquart = values.get(values.size()/4);
                    int Uquart = values.get(3*values.size()/4);
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
                    double mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff =0;
                    double total = 0;
                    double sDev =0.0;
                    for(int i =0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    sDev = sqrt((squareDiff/total));//standard deviation
                    int median = values.get(values.size()/2);
                    int Lquart = values.get(values.size()/4);
                    int Uquart = values.get(3*values.size()/4);
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
                    double mean = countSUM/Double.valueOf(values.size());//mean
                    double squareDiff =0;
                    double total = 0;
                    double sDev =0.0;
                    for(int i =0;i<values.size();i++){
                        squareDiff =Math.pow((values.get(i)-mean),2);
                        total++;
                    }
                    sDev = sqrt((squareDiff/total));//standard deviation
                    float median = values.get(values.size()/2);
                    float Lquart = values.get(values.size()/4);
                    float Uquart = values.get(3*values.size()/4);
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
                    Float successRate = Float.valueOf((successCount / totalCount));
                }
            }
        });
    }

}
