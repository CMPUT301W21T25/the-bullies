package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a MeasurementTrial which extends Trial. Records a float trial result
 */
public class MeasurementTrial extends Trial {
    private Float result;

    /**
     * getter for the result of the trial
     * @return float value which is the result of the trial
     */
    public Float getResult() {
        return result;
    }

    /**
     * records the measurement of the trial
     * @param result
     */
    public void setResult(Float result){
        this.result = result;
    }
}
