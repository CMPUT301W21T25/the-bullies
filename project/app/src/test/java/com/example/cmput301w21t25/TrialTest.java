package com.example.cmput301w21t25;

import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.trials.CountTrial;
import com.example.cmput301w21t25.trials.MeasurementTrial;
import com.example.cmput301w21t25.trials.NonNegCountTrial;
import com.example.cmput301w21t25.trials.Trial;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TrialTest {
    private BinomialTrial mockBinomialTrial() {
        BinomialTrial binomialTrial = new BinomialTrial();
        return binomialTrial;
    }

    private CountTrial mockCountTrial() {
        CountTrial countTrial = new CountTrial();
        return countTrial;
    }

    private NonNegCountTrial mockNonNegTrial() {
        NonNegCountTrial nonNegCountTrial = new NonNegCountTrial();
        return nonNegCountTrial;
    }

    private MeasurementTrial mockMeasurementTrial() {
        MeasurementTrial measurementTrial = new MeasurementTrial();
        return measurementTrial;
    }

    @Test
    void testBinomialTrial() {
        BinomialTrial mockTrial = mockBinomialTrial();
        mockTrial.setResult(true);
        assertTrue(mockTrial.getResult() == true);
    }

    @Test
    void testCountTrial() {
        CountTrial mockTrial = mockCountTrial();
        mockTrial.setResult(3);

        assertTrue(mockTrial.getResult()==3);
    }

    @Test
    void testNonNegTrial() {
        NonNegCountTrial mockTrial = mockNonNegTrial();
        mockTrial.setResult(5);

        assertTrue(mockTrial.getResult()==5);
    }

    @Test
    void testMeasurementTrial() {
        MeasurementTrial mockTrial = mockMeasurementTrial();
        mockTrial.setResult(3.6f);

        assertTrue(mockTrial.getResult()==3.6f);
    }
}
