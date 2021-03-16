package com.example.cmput301w21t25;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class CreateExperimentActivity extends AppCompatActivity {
//(String name, String ownerID, String description, Location region, ArrayList<String> tags, Boolean geoEnabled, Boolean published, String type, Date date)
    EditText experimentName;
    EditText experimentDescription;
    EditText experimentTags;
    EditText type;

    Date experimentDate;
    Location experimentLocation;

    CheckBox published;
    CheckBox geolocationEnabled;

    String experimentOwner;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void onCreate(Bundle passedData){
        super.onCreate(passedData);
        //Change layout
        setContentView(R.layout.activity_home_created);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        //User ID for testing (has owned experiment): fdNzWupOTDKvwkrVHMADau

        //Get the user's name from their profile
        DocumentReference docRef = db.collection("UserProfile").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String type = (String) document.getData().get("type");
                    }
                }
            }
        });
    }
}
