package com.example.cmput301w21t25;
import java.util.ArrayList;

public class User {
    private ArrayList<Experiment> subscribedExperiments;
    private ArrayList<Trial> conductedTrials;
    private ArrayList<Experiment> ownedExperiments;

    private String name;
    private String email;

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private void subscribeTo(Experiment experiment) {
    }

    private void unsubscribeFrom(Experiment experiment) {
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
