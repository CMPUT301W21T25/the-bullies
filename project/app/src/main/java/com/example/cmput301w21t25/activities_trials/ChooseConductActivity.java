package com.example.cmput301w21t25.activities_trials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_qr.MenuQRActivity;
import com.example.cmput301w21t25.activities_qr.RegisterBarcodeActivity;
import com.example.cmput301w21t25.experiments.Experiment;

public class ChooseConductActivity extends AppCompatActivity {

    private String userID;
    private Experiment exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial_type_dialog_fragment);

        Button appInterface = findViewById(R.id.app_interface_trial_button);
        Button qrCodes = findViewById(R.id.qr_code_trial_button);
        Button barcodes = findViewById(R.id.barcode_trial_type_button);

        userID = getIntent().getStringExtra("USER_ID");
        exp = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");

        appInterface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Launches a conduct trial activity based on type of experiment
                Intent switchScreen = null;
                switch (exp.getType()) {
                    case "count":
                        switchScreen = new Intent(ChooseConductActivity.this, ConductCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "nonnegative count":
                        switchScreen = new Intent(ChooseConductActivity.this, ConductNonnegativeCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "binomial":
                        switchScreen = new Intent(ChooseConductActivity.this, ConductBinomialTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "measurement":
                        switchScreen = new Intent(ChooseConductActivity.this, ConductMeasurementTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                }
            }
        });

        qrCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qr = new Intent(ChooseConductActivity.this, MenuQRActivity.class);
                qr.putExtra("USER_ID", userID);
                qr.putExtra("TRIAL_PARENT", exp);
                qr.putExtra("CODE_TYPE", "qr");
                startActivity(qr);
            }
        });

        barcodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qr = new Intent(ChooseConductActivity.this, MenuQRActivity.class);
                qr.putExtra("USER_ID", userID);
                qr.putExtra("TRIAL_PARENT", exp);
                qr.putExtra("CODE_TYPE", "barcode");
                startActivity(qr);
            }
        });

    }
}

/*
addTrialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches a conduct trial activity based on type of experiment
                Intent switchScreen = null;
                switch (exp.getType()) {
                    case "count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "nonnegative count":
                        switchScreen = new Intent(AddTrialActivity.this, ConductNonnegativeCountTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "binomial":
                        switchScreen = new Intent(AddTrialActivity.this, ConductBinomialTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                    case "measurement":
                        switchScreen = new Intent(AddTrialActivity.this, ConductMeasurementTrialActivity.class);
                        switchScreen.putExtra("USER_ID", userID);
                        switchScreen.putExtra("TRIAL_PARENT", exp);
                        startActivity(switchScreen);
                        break;
                }
            }
        });
 */