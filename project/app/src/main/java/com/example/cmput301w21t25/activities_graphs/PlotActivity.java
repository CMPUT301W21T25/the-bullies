package com.example.cmput301w21t25.activities_graphs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.activities_main.SearchExperimentsActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.experiments.Experiment;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * This activity is used to view data plots
 * @author Samadhi Eden
 */
public class PlotActivity extends AppCompatActivity {

    //If you are generating a line graph for a non-negative or measurement experiment, generate the
    //list of plot points using meanByDay()
    //For count trials, countByDay()
    //For binomial trials, successRateByDay

    LineChart lineChart;
    String userID;
    TextView title;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        userID = getIntent().getStringExtra("USER_ID");

        Bundle expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP");
        PlotManager plotManager = new PlotManager();

        setTitle(exp.getName());
        lineChart = (LineChart) findViewById(R.id.line_chart);
        title = findViewById(R.id.Plottitle_text_view);
        String tempTitle = exp.getName() + " LineChart";
        title.setText(tempTitle);
        plotManager.FB_UpdateSummaryViews(exp, lineChart);
    }

}

