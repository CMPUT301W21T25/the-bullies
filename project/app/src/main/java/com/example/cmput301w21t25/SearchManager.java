package com.example.cmput301w21t25;

import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchManager {
    public ArrayList<String> parseKeywords(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, ",");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        //For testing purposes
        for (String i: keywordList) {
            Log.d("TEST_PARSE", i);
        }

        Log.d("CAT", (Integer.toString(keywordList.get(0).length())));
        //End of testing

        return keywordList;
    }

    public ArrayList<Experiment> searchExperiments(String keywords, ArrayList<Experiment> allExperiments) {
        ArrayList<String> keywordList = this.parseKeywords(keywords);
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();
        ArrayList<String> keywordTypeList = new ArrayList<String>();
        ArrayList<String> typeList = new ArrayList<String>();

        typeList.add("binomial");
        typeList.add("measurement");
        typeList.add("count");
        typeList.add("nonnegative count");

        for (int i = 0; i < keywordList.size(); i++) {
            for (String type: typeList) {
                if (keywordList.get(i) == type) {
                    keywordTypeList.add(keywordList.remove(i));
                }
            }
        }

        for (int i = 0; i < allExperiments.size(); i++) {
            String experimentType = allExperiments.get(i).getType();
            for (String type : keywordTypeList) {
                if (type == experimentType) {
                    keywordExperiments.add(allExperiments.remove(i));
                }
            }
        }

        for (int i = 0; i < allExperiments.size(); i++) {
            ArrayList<String> experimentName = this.parseKeywords(allExperiments.get(i).getName());
            for (String titleWord: experimentName) {
                for (String keyword: keywordList) {
                    if (titleWord == keyword) {
                        keywordExperiments.add(allExperiments.remove(i));
                    }
                }
            }
        }
        
        return keywordExperiments;
    }

}
