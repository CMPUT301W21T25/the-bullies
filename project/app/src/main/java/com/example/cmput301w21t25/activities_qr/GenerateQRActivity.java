package com.example.cmput301w21t25.activities_qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRActivity extends AppCompatActivity {


    String encode;
    Bitmap bitmap;

    EditText nonNegIntValue;
    EditText measurementValue;

    Button back;
    Button generateNum;
    Button generateTrue;
    Button generateFalse;
    LinearLayout binomialButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r);

        String userID = getIntent().getStringExtra("USER_ID");
        Experiment trialExperiment = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        String trialType = trialExperiment.getType();

        // EditText fields to input value QR code should hold.
        // Input field takes integers for NonNegInt trials,
        // and floats for Measurement trials. Relevant view
        // is shown, other is hidden. No input is required for
        // Count or Binomial trials, so both input fields are hidden.
        nonNegIntValue = findViewById(R.id.qr_integer_edit_text);
        measurementValue = findViewById(R.id.qr_decimal_edit_text);

        // generateTrue and generateFalse buttons are shown for
        // Binomial trials, generateNum button is shown for all
        // other trials
        back = findViewById(R.id.back_to_qr_menu_button);
        generateNum = findViewById(R.id.generate_qr_code_button);
        binomialButtons = findViewById(R.id.binomial_buttons_layout);
        generateTrue = findViewById(R.id.generate_true_button);
        generateFalse = findViewById(R.id.generate_false_button);

        /* might be unneeded
        nonNegIntValue.setVisibility(View.GONE);
        measurementValue.setVisibility(View.GONE);
        generateNum.setVisibility(View.GONE);
        binomialButtons.setVisibility(View.GONE);
        */

        // ImageView to display generated QR code
        ImageView qrCode = findViewById(R.id.generated_qr_code);
        //qrCode.setImageBitmap(bitmap);

        setupViews(trialType);


    }

    private void setupViews(String trialType) {
        switch (trialType) {
            case "count":
                generateNum.setVisibility(View.VISIBLE);

                break;
            case "nonnegative count":
                generateNum.setVisibility(View.VISIBLE);
                nonNegIntValue.setVisibility(View.VISIBLE);

                break;
            case "measurement":
                generateNum.setVisibility(View.VISIBLE);
                measurementValue.setVisibility(View.VISIBLE);

                break;
            case "binomial":
                binomialButtons.setVisibility(View.VISIBLE);

                break;
        }
    }


    /**
     * Generates a bitmatrix that is encoded into a QR code bitmap
     * @param encode the String value of what should be encoded
     * @return the QR code bitmap
     */
    private Bitmap createBitmap(String encode){

        MultiFormatWriter mfw = new MultiFormatWriter();
        BarcodeEncoder be = new BarcodeEncoder();

        BitMatrix bitMatrix;
        Bitmap bitmap = null;

        try {
            bitMatrix = mfw.encode(encode, BarcodeFormat.QR_CODE, 200, 200);
            bitmap = be.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

}