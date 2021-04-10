package com.example.cmput301w21t25.activities_qr;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterBarcodeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Adding an onClickListener to the button.
    addCityButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Retrieving the city name and the province name from the EditText fields.
            final String cityName = addCityEditText.getText().toString();
            final String provinceName = addProvinceEditText.getText().toString();

            // We use a HashMap to store a key-value pair in firestore. Can you guess why? Because it's a No-SQL database.
            HashMap<String, String> data = new HashMap<>();
            if(cityName.length()>0 && provinceName.length()>0){ // We do not add anything if either of the fields are empty.

                // If there is some data in the EditText field, then we create a new key-value pair.
                data.put("province_name",provinceName);

                // The set method sets a unique id for the document.
                collectionReference
                        .document(cityName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // These are a method which gets executed when the task is successful.
                                Log.d(TAG, "Data addition successful");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // This method gets executed if there is any problem.
                                Log.d(TAG, "Data addition failed" + e.toString());
                            }
                        });

}
