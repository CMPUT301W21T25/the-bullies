package com.example.cmput301w21t25.experiments;

/**
 * this class is for storing measurement experiments
 */
public class MeasurementExperiment extends Experiment {
    private float result;
    public MeasurementExperiment() {
    }

    public float getResult() {
        return result;
    }

    //Test only!!
    public void testOnlySetResult(float result) {this.result = result;}

}
