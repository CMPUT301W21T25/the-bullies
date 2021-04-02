package com.example.cmput301w21t25;

import com.example.cmput301w21t25.experiments.Experiment;

import java.util.ArrayList;

public interface FirestoreExperimentCallback {
    void onCallback(ArrayList<Experiment> list);
}
