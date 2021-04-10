package com.example.cmput301w21t25.activities_qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
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


/**
 * This activity is used to generate a QR code
 */
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
        String codeType = getIntent().getStringExtra("CODE_TYPE");

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

        // ImageView to display generated QR code
        ImageView qrCode = findViewById(R.id.generated_qr_code);

        setupViews(trialType);
        Log.d("DK: Type", trialType);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(GenerateQRActivity.this, MenuQRActivity.class);
                back.putExtra("USER_ID", userID);
                back.putExtra("TRIAL_PARENT", trialExperiment);
                back.putExtra("CODE_TYPE", "qr");
                startActivity(back);
            }
        });

        generateNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encode = setEncode(trialType);
                if (encode != null) {
                    Log.d("DK: encode", encode);
                    bitmap = createBitmap(encode);
                    qrCode.setImageBitmap(bitmap);
                }
                else {
                    Log.d("DK: encode", "encode was null");
                    Toast.makeText(GenerateQRActivity.this, "You must enter a value!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generateTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encode = setEncode(trialType);
                Log.d("DK: encode", encode);
                bitmap = createBitmap(encode);
                qrCode.setImageBitmap(bitmap);
            }
        });

        generateFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encode = "false";
                Log.d("DK: encode", encode);
                bitmap = createBitmap(encode);
                qrCode.setImageBitmap(bitmap);
            }
        });




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

    private String setEncode(String trialType) {
        String encode = null;
        switch (trialType) {
            case "count":
                encode = "1";

                break;
            case "nonnegative count":
                encode = nonNegIntValue.getText().toString();

                break;
            case "measurement":
                encode = measurementValue.getText().toString();

                break;
            case "binomial":
                encode = "true";

                break;
        }
        if (encode.length() < 1) {
            encode = null;
        }
        return encode;
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