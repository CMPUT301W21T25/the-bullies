package com.example.cmput301w21t25.managers;

import com.example.cmput301w21t25.experiments.Experiment;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchManager {

    public SearchManager(){

    }

    public ArrayList<String> parseKeywords(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, ",");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        /*//For testing purposes
        for (String i: keywordList) {
            Log.d("TEST_PARSE", i);
        }

        Log.d("CAT", (Integer.toString(keywordList.get(0).length())));

        //End of testing*/

        return keywordList;
    }

    public ArrayList<String> parseTitle(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, " ");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        /*//For testing purposes
        for (String i: keywordList) {
            Log.d("TEST_PARSE", i);
        }

        Log.d("CAT", (Integer.toString(keywordList.get(0).length())));
        //End of testing*/

        return keywordList;
    }

    public ArrayList<Experiment> searchType(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<String> typeList = new ArrayList<String>();
        ArrayList<String> keywordTypeList = new ArrayList<String>();
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        typeList.add("binomial");
        typeList.add("measurement");
        typeList.add("count");
        typeList.add("nonnegative count");

        //Determines keywords referring to types and places them in their own list
        for (int i = 0; i < keywordList.size(); i++) {
            for (String type: typeList) {
                if (keywordList.get(i).equals(type)) {
                    keywordTypeList.add(keywordList.get(i));
                }
            }
        }

        //Uses type keywords to add experiments with matching types to list
        for (int i = 0; i < allExperiments.size(); i++) {
            String experimentType = allExperiments.get(i).getType();
            nextDocument:
            for (String type : keywordTypeList) {
                if (type.equals(experimentType)) {
                    keywordExperiments.add(allExperiments.get(i));
                    break nextDocument;
                }
            }
        }
        return keywordExperiments;
    }

    public ArrayList<Experiment> searchExperimentName(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Compares keywords with experiment names
        for (int i = 0; i < allExperiments.size(); i++) {
            ArrayList<String> experimentName = this.parseTitle(allExperiments.get(i).getName());
            nextDocument:
            for (String titleWord: experimentName) {
                for (String keyword: keywordList) {
                    if (titleWord.equals(keyword)) {
                        keywordExperiments.add(allExperiments.get(i));
                        break nextDocument;
                    }
                }
            }
        }

        return keywordExperiments;
    }

    public ArrayList<Experiment> searchExperimentKeywords(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Compares (user input) keywords with experiment keywords
        for (int i = 0; i < allExperiments.size(); i++) {
            ArrayList<String> experimentKeywords = allExperiments.get(i).getKeywords();
            nextDocument:
            for (String titleWord: experimentKeywords) {
                for (String keyword: keywordList) {
                    if (titleWord.equals(keyword)) {
                        keywordExperiments.add(allExperiments.get(i));
                        break nextDocument;
                    }
                }
            }
        }

        return keywordExperiments;
    }

    public ArrayList<Experiment> searchExperiments(String keywords, ArrayList<Experiment> allExperiments) {
        //Parse user keyword input
        ArrayList<String> keywordList = this.parseKeywords(keywords);
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Add type matches to keyword experiment list and remove them from all experiment list (as
        //the experiment will already be displayed in search so multiple matches are not of interest)
        ArrayList<Experiment> typeKeywordExperiments = this.searchType(keywordList, allExperiments);
        for (int i = 0; i < typeKeywordExperiments.size(); i++) {
            if (!keywordExperiments.contains(typeKeywordExperiments.get(i))) {
                keywordExperiments.add(typeKeywordExperiments.get(i));
                //allExperiments.remove(typeKeywordExperiments.get(i));
            }
        }

        //The same as with type, but with experiment name matches
        ArrayList<Experiment> experimentNameKeywordExperiments = this.searchExperimentName(keywordList, allExperiments);
        for (int i = 0; i < experimentNameKeywordExperiments.size(); i++) {
            if (!keywordExperiments.contains(experimentNameKeywordExperiments.get(i))) {
                keywordExperiments.add(experimentNameKeywordExperiments.get(i));
                //allExperiments.remove(experimentNameKeywordExperiments.get(i));
            }
        }

        //The same as with type, but with experiment keyword matches
        ArrayList<Experiment> experimentKeywordsKeywordExperiments = this.searchExperimentKeywords(keywordList, allExperiments);
        for (int i = 0; i < experimentKeywordsKeywordExperiments.size(); i++) {
            if (!keywordExperiments.contains(experimentKeywordsKeywordExperiments.get(i))) {
                keywordExperiments.add(experimentKeywordsKeywordExperiments.get(i));
                //allExperiments.remove(experimentKeywordsKeywordExperiments.get(i));
            }
        }

        return keywordExperiments;
    }

}
