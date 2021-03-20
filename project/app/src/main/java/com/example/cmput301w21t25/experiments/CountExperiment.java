package com.example.cmput301w21t25.experiments;

import com.example.cmput301w21t25.experiments.Experiment;

/**
 * this class is used to store count experiments
 */
public class CountExperiment extends Experiment {
    private int result;
    public CountExperiment() {
    }

    public int getResult() {
        return result;
    }

    //Test only!!
    public void testOnlySetResult(int result) {this.result = result;}


}
