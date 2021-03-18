package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a CountTrial which extends Trial. Records a integer trial result
 * Assumption: Th used can keep incrimenting for 1 trail before submitting
 */
public class CountTrial extends Trial{
    private Integer result;

    public Integer result() {
        return result;
    }

    /**
     * @param result records the total count of the trial
     */

    private void addResult(Integer result){
        this.result += result;
    }
}
