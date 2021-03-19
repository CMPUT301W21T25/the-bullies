package com.example.cmput301w21t25.trials;

/**
 * @author Samadhi
 * This trial allows the user to enter a non-negative integer to record
 * NOTE: I deviated from the UML when implementing this bc I saw no clear reason for
 * extending CountTrial
 * TODO: update UML after approval of teammates
 */
public class NonNegCountTrial extends Trial{
    //We can likely get rid of the count to keep track of "out of how many" if we just count
    //how many trials an experiment has, multiply that by the "out of" value
    private Integer positiveCount;
    private Integer result;

    //Don't need this? -EK
    /*private NonNegCountTrial(Integer result){
        this.result = result;
    }*/

    /**
     * getter for the result of the trial
     * @return Integer value which is the result of the trial
     */
    public Integer getResult() {
        return result;
    }

    //these methods are not utilized yet and are subject to change as such have yet to be documented fully
    /**
     * @param result This is the number which will be recorded for the trial
     */
    private void setResult(Integer result){ this.result = result;

        //This should probably be checked when we get the input, not when we try and set it -EK
        /*if(count >= 0){
            this.result = result;
        }
        else{
            //TODO: Show toast for adding a negative number StuPid put pOsiTiVe
        }*/

    }

}
