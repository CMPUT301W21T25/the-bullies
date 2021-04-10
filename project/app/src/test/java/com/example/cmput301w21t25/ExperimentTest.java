package com.example.cmput301w21t25;
import android.widget.ListView;

import com.example.cmput301w21t25.experiments.Experiment;

import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentTest {
    //Build the mock experiment for test
    private Experiment mockExperiment(){
        Experiment experiment = new Experiment();
        return experiment;
    }
    //Test if the date can be set and get properly
    @Test
    void testDate(){
        Experiment experiment = mockExperiment();
        Date date = new Date();
        experiment.setDate(date);
        assertEquals(date,experiment.getDate());
    }
    //Test if the description can be set and get properly
    @Test
    void testDescription(){
        Experiment experiment = mockExperiment();
        String description = "Hello World";
        experiment.setDescription(description);
        assertEquals(description,experiment.getDescription());
    }
    //Test if IsEnded can be set and get properly
    @Test
    void testIsEnded(){
        Experiment experiment = mockExperiment();
        Boolean isEnded = true;
        experiment.setIsEnded(isEnded);
        assertEquals(isEnded,experiment.getIsEnded());
    }
    //Test if minNumTrials can get properly
    @Test
    void testMinNumTrials(){
        Experiment experiment = mockExperiment();
        assertNotEquals(null,experiment.getMinNumTrials());
        assertEquals(0,experiment.getMinNumTrials());
    }
    //Test if name can be set and test properly
    @Test
    void testName(){
        Experiment experiment = mockExperiment();
        experiment.setName("Test");
        assertNotEquals("NoTest",experiment.getName());
        assertEquals("Test",experiment.getName());
    }
    //Test if owner can get properly
    @Test
    void testOwner(){
        Experiment experiment = mockExperiment();
        assertEquals(null,experiment.getOwner());
    }
    //Test if ownerID can get properly
    @Test
    void testOwnerID(){
        Experiment experiment = mockExperiment();
        assertEquals(null,experiment.getOwnerID());
    }
    //Test if experiment status can get properly
    @Test
    void testPublished(){
        Experiment experiment = mockExperiment();
        assertNotEquals(true,experiment.isPublished());
    }
    //Test if the Tags list can be set and get properly
    @Test
    void testTags(){
        Experiment experiment = mockExperiment();
        ArrayList<String> tags = new ArrayList<>();
        tags.add("This");
        tags.add("is");
        tags.add("a");
        tags.add("test");

        experiment.setTags(tags);
        assertNotEquals(null, experiment.getTags());
    }
    //Test if trialKeys list can be set and get properly
    @Test
    void testTrialKeys(){
        Experiment experiment = mockExperiment();
        ArrayList<String> trialKeys = new ArrayList<String>();
        trialKeys.add("key");
        trialKeys.add("word");

        assertNotEquals(trialKeys,experiment.getTrialKeys());
    }
    //Test if the contributor user keys can set and get properly
    @Test
    void testContributorUsersKeys(){
        Experiment experiment = mockExperiment();
        ArrayList<String> contributorUsersKeys = new ArrayList<String>();
        contributorUsersKeys.add("user");
        contributorUsersKeys.add("key");
        assertNotEquals(contributorUsersKeys,experiment.getContributorUsersKeys());
    }
    //Test if hidden user key can set and get properly
    @Test
    void testHiddenUsersKeys(){
        Experiment experiment = mockExperiment();
        ArrayList<String> hiddenUsersKeys = new ArrayList<String>();
        hiddenUsersKeys.add("hid");
        hiddenUsersKeys.add("key");
        assertNotEquals(hiddenUsersKeys,experiment.getHiddenUsersKeys());
    }
    //Test if type can set and get properly
    @Test
    void testType(){
        Experiment experiment = mockExperiment();
        String type = "count";
        experiment.setType(type);
        assertEquals(type,experiment.getType());
    }
    //Test if fb_id can set and get properly
    @Test
    void testFb_id(){
        Experiment experiment = mockExperiment();
        String fb_id = "301";
        experiment.setFb_id(fb_id);
        assertEquals(fb_id,experiment.getFb_id());
    }
    //Test if the region can be set and get properly
    @Test
    void testRegion(){
        Experiment experiment = mockExperiment();
        String region = "Edmonton";
        experiment.setRegion(region);
        assertNotEquals("Canada",experiment.getRegion());
        assertEquals(region,experiment.getRegion());
    }


}
