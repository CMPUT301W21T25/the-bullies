package com.example.cmput301w21t25;
import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.CountExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.experiments.MeasurementExperiment;
import com.example.cmput301w21t25.experiments.NonNegCountExperiment;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.NonNegCountTrial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentTest {

    private BinomialExperiment mockBinomialExperiment() {
        BinomialExperiment experiment = new BinomialExperiment();
        return experiment;
    }

    private NonNegCountExperiment mockNNCountExperiment() {
        NonNegCountExperiment experiment = new NonNegCountExperiment();
        return experiment;
    }

    private CountExperiment mockCountExperiment() {
        CountExperiment experiment = new CountExperiment();
        return experiment;
    }

    private MeasurementExperiment mockMeasurementExperiment() {
        MeasurementExperiment experiment = new MeasurementExperiment();
        return experiment;
    }

    @Test
    void testGeneralExperiment(){
        BinomialExperiment testExperiment = mockBinomialExperiment();
        assertEquals(null, testExperiment.getDescription());

        testExperiment.setDescription("This is a test description");
        assertEquals("This is a test description", testExperiment.getDescription());
    }

    @Test
    void testBinomialExperiment(){
        BinomialExperiment testExperiment = mockBinomialExperiment();
        testExperiment.testOnlySetResult(true);

        assertEquals(true, testExperiment.getResult());
    }
    @Test
    void testNNCountExperiment(){
        NonNegCountExperiment testExperiment = mockNNCountExperiment();
        testExperiment.testOnlySetResult(3);

        assertEquals(3, testExperiment.getResult());

    }
    @Test
    void testCountExperiment(){
        CountExperiment testExperiment = mockCountExperiment();
        testExperiment.testOnlySetResult(5);

        assertEquals(5, testExperiment.getResult());

    }
    @Test
    void testMeasurementExperiment(){
        MeasurementExperiment testExperiment = mockMeasurementExperiment();
        testExperiment.testOnlySetResult(3.5f);

        assertEquals(3.5f, testExperiment.getResult());
    }
}
