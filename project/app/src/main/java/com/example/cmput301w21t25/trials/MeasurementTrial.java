package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a MeasurementTrial which extends Trial. Records a float trial result
 */
public class MeasurementTrial extends Trial {
    private Float measurement;

    /**
     * records the measurement of the trial
     * @param measurement
     */
    private void addResult(Float measurement){
        this.measurement = measurement;
    }
}
