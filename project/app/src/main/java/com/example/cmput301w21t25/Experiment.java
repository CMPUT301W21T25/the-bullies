package com.example.cmput301w21t25;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Experiment { //make abstract

    //private User owner;
    private String description;
    private int minNumTrials;
    private boolean isPublished;
    //private Region region;
    //private ArrayList<User> subscribedUsers; //all currently subscribed users
    //private ArrayList<User> allUsers; //for users that were subscribed, added data, and unsubscribed
    //private ArrayList<Region> geoLocations;
    //private ArrayList<Comment> forum;
    private ArrayList<Trial> trials;

    /**
     * Allows experiment owner to hide trial results submitted by a User.
     * Throws exception if user's trials are already hidden. //Do we need to though?
     */
     //* @param user
     //*      The user who's trials the owner wants to hide
     //*/
    public void hideTrials() {//User user) throws IllegalArgumentException{
        for (Trial trial : trials) {
            if (!trial.isHidden()) {
                trial.setHidden(true);
            }
        }

    }

    /**
     * Allows experiment owner to show trial results submitted by a User.
     * Throws exception if user's trials are already shown. //Again, is this necessary?
     */
    //* @param user
    //*      The user who's trials the owner wants to show
    //*/
    void showTrials() {//User user) {
        for (Trial trial : trials) {
            if (trial.isHidden()) {
                trial.setHidden(false);
            }
        }

    }
    /**
     *
     */
    void showStats() { ... }

    /**
     *
     */
    void plotData() { ... }

    /**
     *
     */
    void createMap() { ... }

}
