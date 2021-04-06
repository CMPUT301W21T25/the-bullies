package com.example.cmput301w21t25.activities_experiments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cmput301w21t25.FirestoreTrialCallback;
import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.activities_graphs.HistogramActivity;
import com.example.cmput301w21t25.histograms.HistogramFragment;
import com.example.cmput301w21t25.location.Maps;
import com.example.cmput301w21t25.managers.SummaryCalculator;
import com.example.cmput301w21t25.managers.TrialManager;
import com.example.cmput301w21t25.trials.Trial;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * This activity is used to view the data of an experiment
 */
public class ExperimentDataActivity extends AppCompatActivity implements HistogramFragment.OnFragmentInteractionListener {
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
    private SummaryCalculator summaryCalulator = new SummaryCalculator();

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

        summaryCalulator.FB_UpdateSummaryViews(exp,LquartilesTextView,UquartilesTextView,medianTextView,
                meanTextView,deviationTextView,successRateTextView,currentTrialsTextView);


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
                        args.putSerializable("TrialList", (Serializable) list);//<----this is the trial list
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

        //----------------------------------GRAPH onCreate-----------------------------
        Button histograms = (Button) findViewById(R.id.viewGraphsButton);
        histograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HistogramFragment().show(getSupportFragmentManager(),"HISTOGRAM");
            }
        });

        //----------------------------------END GRAPH onCreate-------------------------
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

}
