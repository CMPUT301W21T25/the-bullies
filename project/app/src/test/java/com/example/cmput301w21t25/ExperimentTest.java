package com.example.cmput301w21t25;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class ExperimentTest {

    private Experiment mockExperiment() {
        Experiment mockExperiment = new Experiment();

        return mockExperiment;
    }

    private void addTestTrials(Experiment experiment) {
        Trial shownTrial = new Trial();
        Trial hiddenTrial = new Trial();

        shownTrial.setHidden(false);
        hiddenTrial.setHidden(true);

        experiment.addTrial(shownTrial);
        experiment.addTrial(hiddenTrial);
    }


    @Test
    void testAddTrial() {
        Experiment exp = mockExperiment();
        Trial trial = new Trial();

        assertEquals(0, exp.getTrials().size());

        exp.addTrial(trial);

        assertEquals(1, exp.getTrials().size());
    }

    @Test
    void testDeleteTrial() {
        Experiment exp = mockExperiment();
        Trial trial = new Trial();
        exp.addTrial(trial);
        assertEquals(1, exp.getTrials().size());

        exp.deleteTrial(trial);

        assertEquals(0, exp.getTrials().size());
    }

    @Test
    void testShowResults() {
        Experiment exp = mockExperiment();
        addTestTrials(exp);
        exp.showTrials();

        for (Trial trial: exp.getTrials()) {
            assertFalse(trial.isHidden());
        }

        //test if method shows results from a given user
        //test if given user is not present
        //test if trials are already shown
    }

    @Test
    void testHideResults() {
        Experiment exp = mockExperiment();
        addTestTrials(exp);
        exp.hideTrials();

        for (Trial trial: exp.getTrials()) {
            assertTrue(trial.isHidden());
        }
        //Add trials to experiment
        //test if method hides results from a given user
        //test if given user is not present
        //test if trials are already hidden
    }



}
