package com.example.cmput301w21t25.activities_qr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterBarcodeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("ScanCodes");

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
        setContentView(R.layout.activity_register_barcode);

        String userID = getIntent().getStringExtra("USER_ID");
        Experiment trialExperiment = (Experiment) getIntent().getSerializableExtra("TRIAL_PARENT");
        String trialType = trialExperiment.getType();

        // EditText fields to input value QR code should hold.
        // Input field takes integers for NonNegInt trials,
        // and floats for Measurement trials. Relevant view
        // is shown, other is hidden. No input is required for
        // Count or Binomial trials, so both input fields are hidden.
        nonNegIntValue = findViewById(R.id.barcode_integer_edit_text);
        measurementValue = findViewById(R.id.barcode_decimal_edit_text);

        // generateTrue and generateFalse buttons are shown for
        // Binomial trials, generateNum button is shown for all
        // other trials
        back = findViewById(R.id.back_to_barcode_menu_button);
        generateNum = findViewById(R.id.generate_barcode_button);
        binomialButtons = findViewById(R.id.barcode_binomial_buttons_layout);
        generateTrue = findViewById(R.id.barcode_generate_true_button);
        generateFalse = findViewById(R.id.barcode_generate_false_button);

        /* might be unneeded
        nonNegIntValue.setVisibility(View.GONE);
        measurementValue.setVisibility(View.GONE);
        generateNum.setVisibility(View.GONE);
        binomialButtons.setVisibility(View.GONE);
        */

        // ImageView to display generated QR code
        ImageView qrCode = findViewById(R.id.generated_qr_code);

        setupViews(trialType);
        Log.d("DK: Type", trialType);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(RegisterBarcodeActivity.this, MenuQRActivity.class);
                back.putExtra("USER_ID", userID);
                back.putExtra("TRIAL_PARENT", trialExperiment);
                startActivity(back);
            }
        });


    // Adding an onClickListener to the button.
    generateNum.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Retrieving the city name and the province name from the EditText fields.
            //final String cityName = addCityEditText.getText().toString();
            //final String provinceName = addProvinceEditText.getText().toString();

            // We use a HashMap to store a key-value pair in firestore. Can you guess why? Because it's a No-SQL database.
            HashMap<String, Float> data = new HashMap<>();
            if (true) { // We do not add anything if either of the fields are empty.

                // If there is some data in the EditText field, then we create a new key-value pair.
                data.put("value", 4f);

                // The set method sets a unique id for the document.
                collectionReference
                        .document("say_this_is_a_barcode")
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is successful.
                                Log.d("DID_IT_WORK", "Data addition successful");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // This method gets executed if there is any problem.
                                Log.d("NOPE", "Data addition failed" + e.toString());
                            }
                        });
            }
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
}
