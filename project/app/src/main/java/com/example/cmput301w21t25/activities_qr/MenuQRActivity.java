package com.example.cmput301w21t25.activities_qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;

public class MenuQRActivity extends AppCompatActivity {

    // TODO: implement geolocations with QR codes

    String userID;
    String codeType;
    Experiment trialParent;
    TrialManager trialManager;
    FirebaseFirestore db;
    HashMap<String, Float> measurableBarcode = new HashMap<>();
    HashMap<String, Boolean> nonMeasurableBarcode = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_q_r);

        //         Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get a top-level reference to the collection.
        final CollectionReference collectionReference = db.collection("ScanCodes");


        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    //Log.d(TAG, String.valueOf(doc.getData().get("province_name")));
                    String barcode = doc.getId();
                    Float value = (Float) doc.getData().get("value");
                    measurableBarcode.put(barcode, value); // Adding the cities and provinces from FireStore.
                    Log.d("DATA_BASE", barcode);
                }
            }
        });



        final Button generate_qr_code = findViewById(R.id.generate_qr_button);
        final Button scan_qr_code = findViewById(R.id.scan_button);

        userID = getIntent().getStringExtra("USER_ID");
        codeType = getIntent().getStringExtra("CODE_TYPE");
        trialParent = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");

        trialManager = new TrialManager();

        String encode = "1";
        Bitmap bitmap = createBitmap(encode);

        generate_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeType.equals("qr")) {
                    Intent generate = new Intent(MenuQRActivity.this, GenerateQRActivity.class);
                    generate.putExtra("USER_ID", userID);
                    generate.putExtra("TRIAL_PARENT", trialParent);
                    startActivity(generate);
                }
                else {
                    Intent generate = new Intent(MenuQRActivity.this, RegisterBarcodeActivity.class);
                    generate.putExtra("USER_ID", userID);
                    generate.putExtra("TRIAL_PARENT", trialParent);
                    startActivity(generate);
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

    /**
     * Sets up and activates QR scanner
     */
    public void initiateScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MenuQRActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setCaptureActivity(PortraitCaptureActivity.class);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.initiateScan();
    }

    /**
     * Determines what is done after scanning a QR code
     * @param requestCode
     * @param resultCode
     * @param data
     */
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


                addTrial(trialParent.getType(), intentResult.getContents());

                Toast.makeText(getBaseContext(), "New Trial Added", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addTrial(String trialType, String value) {

        GeoPoint geoPoint = null;
        int trialResultInt;
        float trialResultFloat;
        boolean trialResultBool;


        switch (trialType) {
            case "count":
                trialResultInt = Integer.valueOf(value);
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultInt, trialParent, geoPoint);
                break;
            case "nonnegative count":
                trialResultInt = Integer.valueOf(value);
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultInt, trialParent, geoPoint);
                break;
            case "binomial":
                trialResultBool = Boolean.valueOf(value);
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultBool, trialParent, geoPoint);
            case "measurement":
                trialResultFloat = Float.valueOf(value);
                trialManager.FB_CreateMeasurementTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultFloat, trialParent, geoPoint);
                break;
        }

    }

}