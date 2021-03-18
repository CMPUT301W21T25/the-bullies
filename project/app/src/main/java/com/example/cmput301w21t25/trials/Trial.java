package com.example.cmput301w21t25.trials;

import com.example.cmput301w21t25.user.User;

/**
 * This is an abstract class which creates a trial based on which experiment type was used
 * @author Samadhi
 */
public abstract class Trial {

    /****************************************
                    ATTRIBUTES
     ****************************************/
    //private User user;
    private String experimentName;
    private User experimenter;
    //TODO: implement geolocation info for final product!
    //attributes added -YA
    private String user;
    private String experimentOwnerName;

    /****************************************
                CONSTRUCTORS
     ****************************************/

    /**
     * Constructor for Trial
     * @param user the user of the app
     * @param experimenter the creator of the experiment
     * @param experimentName the name of the experiment
     */
    public Trial(User user, User experimenter, String experimentName) {
        //this.user = user;
        this.experimenter = experimenter;
        this.experimentName = experimentName;
    }
    //extra conductor added -YA
    public Trial(String userID,String experimentName,String experimentOwnerName) {
        this.user = userID;
        this.experimentName=experimentName;
        this.experimentOwnerName=experimentOwnerName;
    }
    public Trial() {}

    /****************************************
                    METHODS
     ****************************************/

//    public User getUser() {
//        return user;
//    }
    public String getExperimentName() {
        return experimentName;
    }
    public User getExperimenter() {
        return experimenter;
    }
    public String getUser() {
        return user;
    }
    public String getExperimentOwnerName() {
        return experimentOwnerName;
    }
}
