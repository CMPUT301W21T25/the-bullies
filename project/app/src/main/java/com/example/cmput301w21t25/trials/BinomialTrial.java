package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * Creates a BinomialTrial which extends Trial. Records a boolean trial result
 */
public class BinomialTrial extends Trial {
    private Boolean result;

    /**
     * getter for the result of the trial
     * @return boolean value which is the result of the trial
     */
    public Boolean getResult() {
        return result;
    }

    //this method has not been fully implemented and as such has not been documented yet
    private void setResult(Boolean result){
        this.result = result;
    }
}
