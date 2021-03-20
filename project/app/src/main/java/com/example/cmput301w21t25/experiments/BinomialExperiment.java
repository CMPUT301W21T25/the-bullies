package com.example.cmput301w21t25.experiments;

/**
 * This class is used to store binomial experiments
 */
public class BinomialExperiment extends Experiment {
    private boolean result;
    public BinomialExperiment() {
    }

    public boolean getResult() {
        return result;
    }

    //Only for testing!!
    public void testOnlySetResult(boolean result) {this.result = result;}
}
