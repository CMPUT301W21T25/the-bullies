package com.example.cmput301w21t25.activities_graphs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.HomeOwnedActivity;
import com.example.cmput301w21t25.activities_main.HomeSubbedActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.HistogramManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * This class calculates the graphical representation of the data
 */
public class HistogramActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<String> dates;
    private ArrayList<BarEntry> barEnteries;
    private ArrayList<Integer> xAxis = new ArrayList<>();
    private ArrayList<Integer> yAxis = new ArrayList<>();
    private HistogramManager histogramManager = new HistogramManager();
    private EditText binCount_editText;
    private Button binCount_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        Bundle expBundle = getIntent().getBundleExtra("EXP_BUNDLE");
        Experiment exp = (Experiment) expBundle.getSerializable("EXP");
        String type = exp.getType();

        setTitle(exp.getName());
        barChart = findViewById(R.id.barchart);
        binCount_editText = findViewById(R.id.bin_number_edit_text);
        binCount_button = findViewById(R.id.bin_number_button);


//        binCount_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binCount = binCount_editText.getText();
//            }
//        });

        //TODO: pass in user specified bin number
        histogramManager.FB_UpdateSummaryViews(exp, barChart);

    }

    /**
     * This class will set all information of the graph view. This includes:
     * - title
     * - x and y axis names
     */
    /*public void setInformation(){

    }*/


}
