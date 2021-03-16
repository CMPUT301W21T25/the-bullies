package com.example.cmput301w21t25;

/**
 * @author Samadhi
 * Creates a BinomialTrial which extends Trial. Records a boolean trial result
 */
public class BinomialTrial extends Trial {
    private Boolean result;

    /**
     * adds result to the trial
     * @param result Boolean result of the trial
     */
    private void addResult(Boolean result){
        this.result = result;
    }
}
