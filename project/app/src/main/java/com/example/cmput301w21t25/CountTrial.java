package com.example.cmput301w21t25;

/**
 * @author Samadhi
 * Creates a CountTrial which extends Trial. Records a integer trial result
 * Assumption: Th used can keep incrimenting for 1 trail before submitting
 */
public class CountTrial extends Trial{
    private Integer totalCount;

    /**
     * @param totalCount records the total count of the trial
     */
    private void addResult(Integer totalCount){
        this.totalCount += totalCount;
    }
}
