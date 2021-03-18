package com.example.cmput301w21t25.user;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;

import java.util.ArrayList;

public class User {
    private ArrayList<Experiment> subscribedExperiments;
    private ArrayList<Trial> conductedTrials;
    private ArrayList<Experiment> ownedExperiments;

    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    private void subscribeTo(Experiment experiment) {
        this.subscribedExperiments.add(experiment);
    }

    private void unsubscribeFrom(Experiment experiment) {
        this.subscribedExperiments.remove(experiment);
    }

    private void generateQRCode() {
    }

    private void registerBarCode() {
    }

    private void scanQRCode() {
    }

    private void scanBarCode() {
    }

    private void printQRCode() {
    }
}
