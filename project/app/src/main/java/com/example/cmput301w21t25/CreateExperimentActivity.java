package com.example.cmput301w21t25;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateExperimentActivity extends AppCompatActivity {
    public void onCreate(Bundle passedData){
        super.onCreate(passedData);
        setContentView(R.layout.activity_home_created);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        //User ID for testing (has owned experiment): fdNzWupOTDKvwkrVHMADau
    }

}
