package com.example.cmput301w21t25.trials;

public class NonMeasurableTrial extends Trial{
    private Boolean result;

    /**
     * getter for the result of the trial
     * @return boolean value which is the result of the trial
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * adds result to the trial
     * @param result Boolean result of the trial
     */

    public void setResult(Boolean result){
        this.result = result;
    }}
