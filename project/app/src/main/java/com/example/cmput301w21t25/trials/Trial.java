package com.example.cmput301w21t25.trials;

import com.example.cmput301w21t25.user.User;

import java.util.Date;

/**
 * This is an abstract class which creates a trial based on which experiment type was used
 */
public abstract class Trial {

    /****************************************
                    ATTRIBUTES
     ****************************************/
    //private User user
    private String experimentName;
    private User experimenter;
    //TODO: implement geolocation info for final product!
    //attributes added -YA
    private String userID;
    private String experimentOwnerName;
    //attributes added -EK
    private String experimentID;
    private Boolean published;
    private Date date;
    private String trialId;

    /****************************************
                CONSTRUCTORS
     ****************************************/

    /**
     * Constructor for Trial
     * @param experimenter the creator of the experiment
     * @param experimentName the name of the experiment
     */
    public Trial(User user, User experimenter, String experimentName) {
        //this.user = user;
        this.experimenter = experimenter;
        this.experimentName = experimentName;
    }

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

        this.userID = userID;
        this.experimentName=experimentName;
        this.experimentOwnerName=experimentOwnerName;
        this.experimentID = experimentID;
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
    /**
     * getter for the user
     * @return returns string value which is the user ID
     */
    public String getUser() {
        return userID;
    }
    /**
     * getter for the trial date
     * @return returns Date object that is the date of creation for this trial
     */
    public Date getDate() { return date; }

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
    public User getExperimenter() {
        return experimenter;
    }
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

}
