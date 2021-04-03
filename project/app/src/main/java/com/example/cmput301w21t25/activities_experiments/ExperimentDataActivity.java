package com.example.cmput301w21t25.activities_experiments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.location.Maps;
import com.example.cmput301w21t25.managers.SummaryCalulator;
import com.example.cmput301w21t25.managers.TrialManager;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.Math.sqrt;

/**
 * This activity is used to view the data of an experiment
 */
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

    private double mean;
    private double sDev;
    private double medianDouble;
    private int medianInt;
    private double Lquart;
    private double Uquart;
    private double successRate;
    private SummaryCalulator summaryCalulator = new SummaryCalulator();

    private Maps maps;

    private String type;
    private Experiment exp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_experiment_data);
        exp = (Experiment) getIntent().getSerializableExtra("EXP");
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



//        switch(type){
//            case "count":
//                CountExperiment countParent = (CountExperiment) exp;
//                FB_FetchSummary(countParent);
//                break;
//            case "binomial":
//                BinomialExperiment binomialParent = (BinomialExperiment) exp;
//                FB_FetchSummary(binomialParent);
//                break;
//            case "nonnegative count":
//                NonNegCountExperiment nnCountParent = (NonNegCountExperiment) exp;
//                FB_FetchSummary(nnCountParent);
//                break;
//            case "measurement":
//                MeasurementExperiment measurementParent = (MeasurementExperiment) exp;
//                FB_FetchSummary(measurementParent);
//                break;
//        }

        experimentInfo.setTitle(exp.getName());
        experimentInfo.setSubtitle(formatDate(exp.getDate()));
        descriptionTextView.setText(exp.getDescription());

        minimumTrialsTextView.setText("Minimum Number of Trials: " + Integer.toString(exp.getMinNumTrials()));
        //finish();

        //                                  LOCATION OnCreate
        //-----------------------------------------------------------------------------
        Button goBack = (Button) findViewById(R.id.button3);
        goBack.setVisibility(View.GONE);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().remove(maps).commit();
                Log.i("curtis", "going back");
            }
        });


        //Check if experiment requires a location
        Button viewMap = (Button) findViewById(R.id.viewMapsButton);
        if (exp.isGeoEnabled()) {
            //show 'View Map' button
            viewMap.setVisibility(View.VISIBLE);
        }
        else {
            //hide 'View Map' button
            viewMap.setVisibility(View.GONE);
        }

        //create OnClick for button
        maps = new Maps();
        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch map fragment
                Bundle args = new Bundle();
                TrialManager trialManager = new TrialManager();
                trialManager.FB_FetchPublishedTrial(exp, new FirestoreTrialCallback() {
                    @Override
                    public void onCallback(List<Trial> list) {
                        args.putParcelable("TrialList", list);//<----this is the trial list
                        args.putString("MODE", "Experiment");
                        Fragment mFragment = maps;
                        mFragment.setArguments(args);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame, mFragment).commit();
                    }
                });

            }
        });
        //                                  END LOCATION OnCreate
        //-----------------------------------------------------------------------------
    }


    
    //                                  LOCATION Methods
    //-----------------------------------------------------------------------------

    // ok so i ended up not needing this section for now

    //                                  END LOCATION Methods
    //-----------------------------------------------------------------------------


    /**
     * returns date in the proper format
     * @param date the date that needs to be formated
     * @return
     */
    private String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private Integer countSUM = 0;
//
//    /**
//     * this function fetches all the trials associated with the provided experiment and calculates summaries according to their type
//     * @param parent the experiment whose summaries are to be retrived the datatype of parent is used to determine which override is used
//     */
//    public void FB_FetchSummary(CountExperiment parent){
//        ArrayList<String>keys = parent.getTrialKeys();
//        ArrayList<Integer> values = new ArrayList<Integer>();
//        CollectionReference docRef = db.collection("TrialDocs");
//        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(keys.contains(document.getId())){
//                            CountTrial trial = document.toObject(CountTrial.class);
//                            values.add(trial.getResult());
//                            countSUM += trial.getResult();//total
//                        }
//                    }
//                    if (values.size() > 0) {
//                        mean = countSUM / (double) values.size();//mean
//                    }
//                    double squareDiff =0;
//                    double total = 0;
//                    for(int i =0;i<values.size();i++){
//                        squareDiff += Math.pow((values.get(i)-mean),2);
//                        total++;
//                    }
//                    if (total != 0) {
//                        sDev = sqrt((squareDiff/total));//standard deviation
//                    }
//
//                    else { sDev = 0; }
//                    medianInt = calculateMedianInt(values);
//                    if (values.size() > 0) {
//                        Lquart = values.get(values.size()/4);
//                        Uquart = values.get(3*values.size()/4);
//                    }
//                    showNonBinomialStats(values.size());
//                }
//            }
//        });
//    }
//    public void FB_FetchSummary(NonNegCountExperiment parent){
//        ArrayList<String>keys = parent.getTrialKeys();
//        ArrayList<Integer> values = new ArrayList<Integer>();
//        CollectionReference docRef = db.collection("TrialDocs");
//        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                Log.d("BEGINNING","WHERE YOU AT");
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(keys.contains(document.getId())){
//                            CountTrial trial = document.toObject(CountTrial.class);
//                            values.add(trial.getResult());
//                            countSUM += trial.getResult();//total
//                        }
//                    }
//                    if (values.size() > 0) {
//                        mean = countSUM/ (double) values.size();//mean
//                    }
//                    double squareDiff =0;
//                    double total = 0;
//                    for(int i = 0; i < values.size(); i++){
//                        squareDiff += Math.pow((values.get(i)-mean),2);
//                        total++;
//                    }
//                    if (total != 0) {
//                        sDev = sqrt((squareDiff/total));//standard deviation
//                    }
//                    else { sDev = 0; }
//                    medianInt = calculateMedianInt(values);
//                    if (values.size() > 0) {
//                        Lquart = values.get(values.size()/4);
//                        Uquart = values.get(3*values.size()/4);
//                    }
//                    showNonBinomialStats(values.size());
//
//                }
//            }
//        });
//    }
//    private Float countSUMF = 0f;
//    public void FB_FetchSummary(MeasurementExperiment parent){
//        ArrayList<String>keys = parent.getTrialKeys();
//        ArrayList<Float> values = new ArrayList<Float>();
//        CollectionReference docRef = db.collection("TrialDocs");
//        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(keys.contains(document.getId())){
//                            MeasurementTrial trial = document.toObject(MeasurementTrial.class);
//                            values.add(trial.getResult());
//                            countSUMF += trial.getResult();//total
//                        }
//                    }
//                    if (values.size() > 0) {
//                        mean = countSUMF/ (double) values.size();//mean
//                    }
//                    double squareDiff = 0;
//                    double total = 0;
//                    for(int i = 0;i<values.size();i++){
//                        squareDiff += Math.pow((values.get(i)-mean),2);
//                        total++;
//                    }
//                    if (total != 0) {
//                        sDev = sqrt((squareDiff/total));//standard deviation
//                    }
//                    else { sDev = 0; }
//                    medianDouble = calculateMedianFloat(values);
//                    Log.d("MEDIAN_MEASUREMENT", String.valueOf(medianDouble));
//                    if (values.size() > 0) {
//                        Lquart = values.get(values.size()/4);
//                        Uquart = values.get(3*values.size()/4);
//                    }
//                    showNonBinomialStats(values.size());
//                }
//            }
//        });
//    }
//    private Integer successCount = 0;
//    private Integer totalCount = 0;
//    public void FB_FetchSummary(BinomialExperiment parent){
//        ArrayList<String>keys = parent.getTrialKeys();
//        ArrayList<Boolean> values = new ArrayList<Boolean>();
//        CollectionReference docRef = db.collection("TrialDocs");
//        docRef.whereEqualTo("published",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        if(keys.contains(document.getId())){
//                            BinomialTrial trial = document.toObject(BinomialTrial.class);
//                            values.add(trial.getResult());
//                            if (trial.getResult()==true){
//                                successCount++;//true
//                            }
//                            totalCount++;//total
//                        }
//                    }
//                    if (totalCount != 0) {
//                        successRate = (float) successCount / (float) totalCount;
//                    }
//                    else successRate = 0;
//                    showBinomialStats(values.size());
//                }
//            }
//        });
//    }

    /**
     * Calculates the experiment's median. Used for the measurement experiment.
     * @param arrayList
     * An ArrayList of trials that have been uploaded to the experiment
     * @return
     * The value at the calculated index
     */
    public Double calculateMedianFloat(ArrayList<Float> arrayList)
    {
        if (arrayList.size() == 0) return Double.NEGATIVE_INFINITY;

        //Sort the array
        Collections.sort(arrayList);

        //Check is there's a midpoint
        if (arrayList.size() % 2 != 0) {
            return (double) arrayList.get(arrayList.size() / 2);
        }

        //Else calculate the average of inner indices
        return (double) arrayList.get((int)(Math.floor(arrayList.size() / 2) + Math.ceil(arrayList.size() / 2)) / 2);
    }

    public int calculateMedianInt(ArrayList<Integer> arrayList)
    {
        if (arrayList.size() == 0) return -1;

        //Sort the array
        Collections.sort(arrayList);

        //Check is there's a midpoint
        if (arrayList.size() % 2 != 0) {
            return arrayList.get(arrayList.size() / 2);
        }

        //Else calculate the average of inner indices
        return arrayList.get((int)(Math.floor(arrayList.size() / 2) + Math.ceil(arrayList.size() / 2)) / 2);
    }

    /**
     * used to show stats of binomial trial
     */
    public void showBinomialStats(int numTrials) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        currentTrialsTextView.setText("Current Number of Trials: " + Integer.toString(numTrials));

        medianTextView.setText("Median: N/A");
        meanTextView.setText("Mean: N/A");
        deviationTextView.setText("Standard Deviation: N/A");
        LquartilesTextView.setText("LQuartiles: N/A");
        UquartilesTextView.setText("UQuartiles: N/A");
        successRateTextView.setText("Success Rate: " + decimalFormat.format(successRate));
    }

    public void showNonBinomialStats(int numTrials) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        currentTrialsTextView.setText("Current Number of Trials: " + Integer.toString(numTrials));

        if (numTrials <= 0) {
            medianTextView.setText("Median: N/A");
            meanTextView.setText("Mean: N/A");
            deviationTextView.setText("Standard Deviation: N/A");
            LquartilesTextView.setText("LQuartiles: N/A");
            UquartilesTextView.setText("UQuartiles: N/A");
            successRateTextView.setText("Success Rate: N/A");
        }

        else {
            if (type.equals("measurement")) {
                Log.d("MEDIAN_MEASUREMENT_2", String.valueOf(medianDouble));
                medianTextView.setText("Median: " + decimalFormat.format(medianDouble));
            }
            else {
                Log.d("MEDIAN_MEASUREMENT_2", String.valueOf(medianDouble));
                medianTextView.setText("Median: " + Integer.toString(medianInt));
            }
            meanTextView.setText("Mean: " + decimalFormat.format(mean));
            deviationTextView.setText("Standard Deviation: " + decimalFormat.format(sDev));
            LquartilesTextView.setText("LQuartiles: " + decimalFormat.format(Lquart));
            UquartilesTextView.setText("UQuartiles: " + decimalFormat.format(Uquart));
            successRateTextView.setText("Success Rate: N/A");
        }
    }
}
