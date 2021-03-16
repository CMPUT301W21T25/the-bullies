package com.example.cmput301w21t25;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Samadhi
 * This trial allows the user to enter a non-negative integer to record
 * NOTE: I deviated from the UML when implementing this bc I saw no clear reason for
 * extending CountTrial
 * TODO: update UML after approval of teammates
 */
public class NonNegCountTrial extends Trial{
    private Integer positiveCount;
    private Integer totalCount;

    private NonNegCountTrial(Integer totalCount){
        this.totalCount = totalCount;
    }

    /**
     * @param count This is the number which will be recorded for the trial
     */
    private void addResult(Integer count){
        if(count >= 0){
            this.positiveCount = count;
        }
        else{
            //TODO: Show toast for adding a negative number StuPid put pOsiTiVe
        }

    }

}
