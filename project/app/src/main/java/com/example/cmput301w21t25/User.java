package com.example.cmput301w21t25;
import java.util.ArrayList;

public class User {
    private ArrayList<Experiment> subscribedExperiments;
    private ArrayList<Trial> conductedTrials;
    private ArrayList<Experiment> ownedExperiments;

    private String userName;

    public User() {
        //test
        userName="testdummy";
    }
    public String getUserName() {
        return userName;
    }

    private void subscribeTo(Experiment experiment) {
    }

    private void unsubscribeFrom(Experiment experiment) {
    }

    private void conductTrial() {
    }

    private void uploadTrial() {
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
