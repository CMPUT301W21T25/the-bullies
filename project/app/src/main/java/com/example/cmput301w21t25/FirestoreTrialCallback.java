package com.example.cmput301w21t25;

import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public interface FirestoreTrialCallback {
    void onCallback(List<Trial> list);
}
