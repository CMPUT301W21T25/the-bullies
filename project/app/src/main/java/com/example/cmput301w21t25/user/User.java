package com.example.cmput301w21t25.user;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.trials.Trial;

import java.util.ArrayList;

/**
 * @author Curtis Kennedy
 * This is a user object that will hold user related data from the databse
 */
public class User {
//    private ArrayList<Experiment> subscribedExperiments;
//    private ArrayList<Trial> conductedTrials;
//    private ArrayList<Experiment> ownedExperiments;

    private String name;
    private String email;


    public User(){
    }

    /**
     * Creates a user object this will be removed
     * @param name
     * @param email
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * gets the name of the experiment
     * @return a string of the name of the experiment
     */
    public String getName() {
        return name;
    }

    /**
     * set the name of the experiment. this is a hold over and will be deleted
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    //these methods are not utilized yet and are subject to change as such have yet to be documented
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

//    private void subscribeTo(Experiment experiment) {
//        this.subscribedExperiments.add(experiment);
//    }
//
//    private void unsubscribeFrom(Experiment experiment) {
//        this.subscribedExperiments.remove(experiment);
//    }

//    private void generateQRCode() {
//    }
//
//    private void registerBarCode() {
//    }
//
//    private void scanQRCode() {
//    }
//
//    private void scanBarCode() {
//    }
//
//    private void printQRCode() {
//    }
}
