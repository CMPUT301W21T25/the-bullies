package com.example.cmput301w21t25.trials;

import android.location.Location;
import android.os.Parcelable;

import com.example.cmput301w21t25.user.User;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.Date;

/**
 * This is an abstract class which creates a trial based on which experiment type was used
 */
public class Trial implements Serializable {

    /****************************************
                    ATTRIBUTES
     ****************************************/
    private GeoPoint geoPoint;
    private Date date;
    private String experiment;
    private String experimentName;
    private String experimentOwnerName;
    private Boolean published;
    private String user;
    private String trialId;
    private String type;

    /****************************************
                CONSTRUCTORS
     ****************************************/

//    /**
//     * Constructor for Trial
//     * @param experimenter the creator of the experiment
//     * @param experimentName the name of the experiment
//     */
//    public Trial(User user, User experimenter, String experimentName) {
//        //this.user = user;
//        this.experimenter = experimenter;
//        this.experimentName = experimentName;
//    }

    //extra constructor added -YA //extra attributes added -EK

    /**
     * Constructor for Trial that uses the new user attribute whcih is a string
     * @author Yalmaz
     * @param userID
     * @param experimentName
     * @param experimentOwnerName
     * @param experimentID
     * @param published
     */
    public Trial(String userID, String experimentName, String experimentOwnerName, String experimentID, Boolean published, Date date) {

        this.user = userID;
        this.experimentName=experimentName;
        this.experimentOwnerName=experimentOwnerName;
        this.experiment = experimentID;
        this.published = published;
        this.date = date;
    }
    public Trial() {}

    /****************************************
                    METHODS
     ****************************************/
    /**
     * getter for the experiment name
     * @return returns string value which is the experiment name
     */
    public String getExperimentName() {
        return experimentName;
    }

    public String getExperiment() {
        return experiment;
    }

    /**
     * getter for the user
     * @return returns string value which is the user ID
     */

    public String getUser() {
        return user;
    }
    /**
     * getter for the trial date
     * @return returns Date object that is the date of creation for this trial
     */
    public Date getDate() { return date; }

    public void setPublished(Boolean published) { this.published = published; }

    public String getTrialId() {
        return trialId;
    }

    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    public Boolean getPublished() {
        return published;
    }

    //these methods are not fully implement and thus have not been documented yet
    public String getExperimentOwnerName() {
        return experimentOwnerName;
    }

    //These classes are for testing purposes and should not be called in regular classes
    // - Samadhi
    public void testOnlySetExperimentName(String experimentName){
        this.experimentName = experimentName;
    }
    public void testOnlySetPublished(Boolean published){
        this.published = published;
    }
    public boolean testOnlyGetPublished(){return published;}

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
