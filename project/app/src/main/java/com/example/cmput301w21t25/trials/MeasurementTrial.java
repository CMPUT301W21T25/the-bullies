package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a MeasurementTrial which extends Trial. Records a float trial result
 */
public class MeasurementTrial extends Trial {
    private Float result;

    public Float getResult() {
        return result;
    }

    /**
     * records the measurement of the trial
     * @param result
     */
    private void setResult(Float result){
        this.result = result;
    }

}
