package com.example.cmput301w21t25;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExperimentTest {

    User user1 = new User();
    User user2 = new User();
    User user3 = new User();

    private Experiment mockExperiment() {
        Experiment mockExperiment = new BinomialExperiment();
        addTestTrials(mockExperiment);
        return mockExperiment;
    }

    private void addTestTrials(Experiment experiment) {
        Trial user1Trial = new Trial(user1);
        Trial user2Trial1 = new Trial(user2);
        Trial user2Trial2 = new Trial(user2);

        experiment.addTrial(user1Trial);
        experiment.addTrial(user2Trial1);
        experiment.addTrial(user2Trial2);
    }


    @Test
    void testAddTrial() {
        Experiment exp = mockExperiment();
        Trial trial = new Trial(new User());

        assertEquals(3, exp.getTrials().size());

        exp.addTrial(trial);

        assertEquals(4, exp.getTrials().size());
    }

    @Test
    void testDeleteTrial() {
        Experiment exp = mockExperiment();

        assertEquals(3, exp.getTrials().size());

        exp.deleteTrial(exp.getTrials().get(0));

        assertEquals(2, exp.getTrials().size());
    }

    @Test
    void testHideTrials() {
        Experiment exp = mockExperiment();
        assertEquals(0, exp.getHiddenTrials().size());

        exp.hideTrials(user1);

        for (Trial trial: exp.getTrials()) {
            assertFalse(trial.getUser() == user1);
        }

        for (Trial trial: exp.getHiddenTrials()) {
            assertTrue(trial.getUser() == user1);
        }

        assertEquals(1, exp.getHiddenTrials().size());
        assertEquals(2, exp.getTrials().size());
        //test if given user is not present
        //test if trials are already hidden

        //exp.hideTrials(user3);


    }

    @Test
    void testShowTrials() {
        Experiment exp = mockExperiment();
        exp.hideTrials(user2);
        assertEquals(1, exp.getTrials().size());

        for (Trial trial: exp.getTrials()) {
            assertTrue(trial.getUser() == user1);
        }

        exp.showTrials(user2);
        assertEquals(3, exp.getTrials().size());
        assertEquals(0, exp.getHiddenTrials().size());

        //test if given user is not present
        //test if trials are already shown

        exp.showTrials(user3);
    }

    @Test
    void testShowAllTrials() {
        Experiment exp = mockExperiment();
        exp.hideTrials(user1);
        exp.hideTrials(user2);

        assertEquals(0, exp.getTrials().size());

        exp.showAllTrials();

        assertEquals(3, exp.getTrials().size());

        //test if trials are already shown
    }



}
