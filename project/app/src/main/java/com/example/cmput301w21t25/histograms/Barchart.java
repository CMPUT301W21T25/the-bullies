package com.example.cmput301w21t25.histograms;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.github.mikephil.charting.charts.BarChart;

public class Barchart extends AppCompatActivity {
    BarChart barChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histogram_barchart);

        barChart = findViewById(R.id.barchart);
    }
}
