package com.example.cmput301w21t25.activities_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;

public class GenerateQRActivity extends AppCompatActivity {

    String userID;
    Experiment trialParent;
    String parentType;

    String encode;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r);

        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        parentType = trialParent.getType();

        // EditText fields to input value QR code should hold.
        // Input field takes integers for NonNegInt trials,
        // and floats for Measurement trials. Relevant view
        // is shown, other is hidden. No input is required for
        // Count or Binomial trials, so both input fields are hidden.
        EditText nonNegIntValue = findViewById(R.id.qr_integer_edit_text);
        EditText measurementValue = findViewById(R.id.qr_decimal_edit_text);

        // ImageView to display generated QR code
        ImageView qrCode = findViewById(R.id.generated_qr_code);
        //qrCode.setImageBitmap(bitmap);


        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");


    }
}