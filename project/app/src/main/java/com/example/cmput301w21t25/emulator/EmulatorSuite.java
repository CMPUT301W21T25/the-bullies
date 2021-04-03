package com.example.cmput301w21t25.emulator;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class EmulatorSuite {
    public FirebaseFirestore firestore;

    public EmulatorSuite() {
    }

    public EmulatorSuite(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    public void useEmulator() {
        // [START fs_emulator_connect]
        // 10.0.2.2 is the special IP address to connect to the 'localhost' of
        // the host computer from an Android emulator.
        firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("10.0.2.2", 8080);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.setFirestoreSettings(settings);
        // [END fs_emulator_connect]
    }

    public void useFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void updateDoc() {
        firestore.collection("newCollection").document("newDocument").update("field1", "newval")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("curtis", "success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("curtis", "failure: "+ e);
                    }
                });
    }

}
