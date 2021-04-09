package com.example.cmput301w21t25.activities_qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewCreatedExperimentActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.firebase.firestore.GeoPoint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.AbstractMap;

public class GenerateQRActivity extends AppCompatActivity {

    String userID;
    Experiment trialParent;
    TrialManager trialManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_q_r);

        final Button generate_qr_code = findViewById(R.id.generate_qr_button);
        final Button scan_qr_code = findViewById(R.id.scan_button);

        userID = getIntent().getStringExtra("USER_ID");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        trialManager = new TrialManager();

        String encode = "1";

        Bitmap bitmap = createBitmap(encode);

        generate_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    Intent showQRCode = new Intent(GenerateQRActivity.this, DisplayQRActivity.class);
                    showQRCode.putExtra("QR_CODE", bitmap);
                    startActivity(showQRCode);
                }
                else {
                    Toast.makeText(GenerateQRActivity.this, "No QR code generated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateScan();
            }
        });

    }

    /**
     * Generates a bitmatrix that is encoded into a QR code bitmap
     * @param encode the String value of what should be encoded
     * @return the QR code bitmap
     */
    public Bitmap createBitmap(String encode){

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

    public void initiateScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(GenerateQRActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setCaptureActivity(PortraitCaptureActivity.class);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //messageText.setText(intentResult.getContents());
                //messageFormat.setText(intentResult.getFormatName());

                GeoPoint geoPoint = null;

                int trialResult = Integer.valueOf(intentResult.getContents());

                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResult, trialParent, geoPoint);
                Toast.makeText(getBaseContext(), "New Trial Added", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}