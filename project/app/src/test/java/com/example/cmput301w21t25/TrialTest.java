package com.example.cmput301w21t25;

import com.example.cmput301w21t25.trials.MeasurableTrial;
import com.example.cmput301w21t25.trials.NonMeasurableTrial;
import com.example.cmput301w21t25.trials.Trial;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class TrialTest {
    //Set up the mock trial for test
    private Trial mockTrial(){
        Trial trial = new Trial("new","trial test","tester","experiment",false,new Date());
        return trial;
    }
    //Test if nothing entered the trial is empty
    @Test
    void testEmptyTrial(){
        Trial emptyTiral = new Trial();
        assertEquals(null, emptyTiral.getUser());
        assertEquals(null,emptyTiral.getExperimentName());
        assertEquals(null,emptyTiral.getExperimentOwnerName());
        assertEquals(null,emptyTiral.getExperiment());
        assertEquals(null,emptyTiral.getPublished());
        assertEquals(null,emptyTiral.getDate());
    }
    //Test if user id set up properly
    @Test
    void testUserId(){
        Trial trial = mockTrial();
        assertEquals("new",trial.getUser());
    }
    //Test if the experiment name set up properly
    @Test
    void testExperimentName(){
        Trial trial = mockTrial();
        assertEquals("trial test",trial.getExperimentName());
        trial.testOnlySetExperimentName("tiral test 2");
        assertEquals("tiral test 2",trial.getExperimentName());
    }
    //Test if experiment owner name set up properly
    @Test
    void testExperimentOwnerName(){
        Trial trial = mockTrial();
        assertEquals("tester",trial.getExperimentOwnerName());
    }
    //Test if experiment id set up properly
    @Test
    void testExperimentId(){
        Trial trial = mockTrial();
        assertEquals("experiment",trial.getExperiment());
    }
    //Test if published status set up properly
    @Test
    void testPublished(){
        Trial trial = mockTrial();
        assertEquals(false,trial.getPublished());
        trial.setPublished(true);
        assertNotEquals(false,trial.getPublished());
    }
    //Test the date set up properly
    @Test
    void testDate(){
        Trial trial = mockTrial();
        assertNotEquals(0,trial.getDate());
    }
    //Test if measurable trial set up properly
    @Test
    void testMeasurableTrial(){
        MeasurableTrial mTrial = new MeasurableTrial();
        Float measure = 1.123f;
        mTrial.setResult(measure);
        assertEquals(measure,mTrial.getResult());
    }
    //Test if the non measurable trial set up properly
    @Test
    void testNonMeasurableTrial(){
        NonMeasurableTrial nonMTrial = new NonMeasurableTrial();
        nonMTrial.setResult(true);
        assertNotEquals(false,nonMTrial.getResult());
    }
 }
