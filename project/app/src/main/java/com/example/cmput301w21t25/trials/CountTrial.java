package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a CountTrial which extends Trial. Records a integer trial result
 * Assumption: Th used can keep incrimenting for 1 trail before submitting
 */
public class CountTrial extends Trial{
    private Integer result;

    /**
     * getter for the result of the trial
     * @return integer value which is the result of the trial
     */
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result){ this.result = result; }


}
