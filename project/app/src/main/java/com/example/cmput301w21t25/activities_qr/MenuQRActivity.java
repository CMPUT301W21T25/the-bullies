package com.example.cmput301w21t25.activities_qr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.activities_main.SearchExperimentsActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.TrialManager;
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
    HashMap<String, Double> measurableBarcode = new HashMap<>();
    HashMap<String, Boolean> nonMeasurableBarcode = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_q_r);

        /*setup the custom toolbar!
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("ScanCodes");

        // Now listening to all the changes in the database and get notified, note that offline support is enabled by default.
        // Note: The data stored in Firestore is sorted alphabetically and per their ASCII values. Therefore, adding a new city will not be appended to the list.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // clear the old list
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    String barcode = doc.getId();
                    String type = (String) doc.getData().get("type");
                    if (type.equals("binomial")) {
                        Boolean value = Boolean.valueOf(doc.getData().get("value").toString());
                        nonMeasurableBarcode.put(barcode, value);
                    }
                    else {
                        Double value = Double.valueOf(doc.getData().get("value").toString());
                        measurableBarcode.put(barcode, value);
                    }

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
                    generate.putExtra("CODE_TYPE", "qr");
                    startActivity(generate);
                }
                else {
                    Intent generate = new Intent(MenuQRActivity.this, RegisterBarcodeActivity.class);
                    generate.putExtra("USER_ID", userID);
                    generate.putExtra("TRIAL_PARENT", trialParent);
                    generate.putExtra("CODE_TYPE", "barcode");
                    startActivity(generate);
                }

            }
        });

        scan_qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeType.equals("qr")) {
                    initiateQRScan();
                }
                else {
                    initiateBarcodeScan();
                }
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
    public void initiateQRScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MenuQRActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setCaptureActivity(PortraitCaptureActivity.class);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan QR Code");
        intentIntegrator.initiateScan();
    }

    public void initiateBarcodeScan() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MenuQRActivity.this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setCaptureActivity(PortraitCaptureActivity.class);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan Barcode");
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

                if (codeType.equals("qr")) {
                    addTrial(trialParent.getType(), intentResult.getContents());
                }
                else {
                    if (trialParent.getType().equals("binomial")) {
                        if (nonMeasurableBarcode.containsKey(intentResult.getContents())) {
                            Boolean temp = (Boolean) nonMeasurableBarcode.get(intentResult.getContents());
                            addTrial(trialParent.getType(), temp.toString());
                        }
                        else Toast.makeText(getBaseContext(), "Barcode Not Registered", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (measurableBarcode.containsKey(intentResult.getContents())) {
                            Double temp = (Double) measurableBarcode.get(intentResult.getContents());
                            addTrial(trialParent.getType(), temp.toString());
                        }
                        else Toast.makeText(getBaseContext(), "Barcode Not Registered", Toast.LENGTH_SHORT).show();
                    }
                }

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
                Double temp = Double.parseDouble(value);
                trialResultInt = (int) Math.round(temp);
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultInt, trialParent, geoPoint);
                break;
            case "nonnegative count":
                Double temp2 = Double.parseDouble(value);
                trialResultInt = (int) Math.round(temp2);
                trialManager.FB_CreateCountTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultInt, trialParent, geoPoint);
                break;
            case "binomial":
                trialResultBool = Boolean.parseBoolean(value);
                trialManager.FB_CreateBinomialTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultBool, trialParent, geoPoint);
                break;
            case "measurement":
                trialResultFloat = Float.parseFloat(value);
                trialManager.FB_CreateMeasurementTrial(userID, trialParent.getFb_id(), trialParent.getName(), trialParent.getOwner(), false, trialResultFloat, trialParent, geoPoint);
                break;
        }

    }

    /**
     * This event is menu setup!
     * @param menu this is the menu being integrated
     * @return true to indicate there is a menu (return false to turn off)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(MenuQRActivity.this, CreatedExperimentsActivity.class);
                home.putExtra("USER_ID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(MenuQRActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("USER_ID", userID);
                user_settings.putExtra("prevScreen", "QR");
                user_settings.putExtra("CODE_TYPE", codeType);
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}