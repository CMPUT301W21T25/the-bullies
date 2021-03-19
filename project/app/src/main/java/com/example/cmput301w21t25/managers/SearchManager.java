package com.example.cmput301w21t25.managers;

import android.util.Log;

import com.example.cmput301w21t25.experiments.Experiment;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Eden
 */
public class SearchManager {

    public SearchManager(){ }

    /**
     * Parses string of user input keywords in order to be compared with experiment keywords
     * @param keywords
     * A user inputted String, assumed to be separated by commas (more rigorous input validation
     * will take place for part 4)
     * @return
     * Parsed keywords, an ArrayList of Strings
     */
    public ArrayList<String> parseKeywords(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        //Split keywords at commas
        StringTokenizer splitKeywords = new StringTokenizer(keywords, ",");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            //Checks that it's not a blank string
            if (keyword.trim().length() > 0) {
                //Adds a stripped word to ArrayList with no leading/trailing whitespace
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        return keywordList;
    }

    /**
     * The same as the above method, but it splits words with " " as the delimiter, as we
     * will require Experiment titles to have no special characters, only spaces
     * @param keywords
     * The String that is to be parsed into an ArrayList of Strings, here it is the experiment title
     * @return
     * An ArrayList of strings
     */
    public ArrayList<String> parseTitle(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, " ");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim().toLowerCase());
            }
        }

        return keywordList;
    }

    /**
     * Searches ArrayList of Strings for types, then compares them to types of Experiments in browse
     * list
     * @param keywordList
     * User inputted String of keywords parsed into an ArrayList of Strings
     * @param allExperiments
     * An ArrayList of all Experiments that are unsubscribed to by the current User
     * @return
     * An ArrayList of experiments that had a keyword match
     */
    public ArrayList<Experiment> searchType(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<String> typeList = new ArrayList<String>();
        ArrayList<String> keywordTypeList = new ArrayList<String>();
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Type Strings exactly as they are in Experiment docs/attributes
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
            //Note that for all search methods, if a break appears, it is to prevent an Experiment
            //from being added to a list twice, though it is redundant in this function as an
            //Experiment only has one type, though it also saves on unnecessary comparisons once
            //a match is found
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

    /**
     * Compares User inputted keywords to compare parsed Experiment titles
     * @param keywordList
     * User inputted String of keywords parsed to be an ArrayList of Strings
     * @param allExperiments
     * An ArrayList of all Experiment the User is currently not subscribed to
     * @return
     * An ArrayList of Experiments that had a keyword match
     */
    public ArrayList<Experiment> searchExperimentName(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Compares keywords with experiment names
        for (int i = 0; i < allExperiments.size(); i++) {
            //Calls parseTitle() to convert Experiment title to ArrayList of Strings
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

    /**
     * Compares User inputted keywords to keywords assigned to an Experiment at its creation
     * @param keywordList
     * A User inputted String of keywords parsed into an ArrayList of Strings
     * @param allExperiments
     * An ArrayList of Experiments that had a keyword match
     * @return
     */
    public ArrayList<Experiment> searchExperimentKeywords(ArrayList<String> keywordList, ArrayList<Experiment> allExperiments) {
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Compares (user input) keywords with experiment keywords
        for (int i = 0; i < allExperiments.size(); i++) {
            //TODO: change to experiemntTagsssgsg
            ArrayList<String> experimentKeywords = allExperiments.get(i).getTags();
            nextDocument:
            if(experimentKeywords != null){
                for (String titleWord: experimentKeywords) {
                    for (String keyword: keywordList) {
                        if (titleWord.equals(keyword)) {
                            keywordExperiments.add(allExperiments.get(i));
                            break nextDocument;
                        }
                    }
                }
            }
        }

        return keywordExperiments;
    }

    /**
     * A search function that calls a type search, a title search, and a keyword search, comparing
     * User input to the respective fields to refine the browse list of Experiments the User is not
     * subscribed to
     * @param keywords
     * A User inputted String of keywords
     * @param allExperiments
     * An ArrayList of all Experiments the current User is not subscribed to
     * @return
     * An ArrayList of Experiments that had a keyword match
     */
    public ArrayList<Experiment> searchExperiments(String keywords, ArrayList<Experiment> allExperiments) {
        //Log.d("SearchManager PASS", String.valueOf(3));
        //Parse user keyword input
        ArrayList<String> keywordList = this.parseKeywords(keywords);
        Log.d("KEYWORD_LIST", String.valueOf(keywordList));
        ArrayList<Experiment> keywordExperiments = new ArrayList<Experiment>();

        //Add type matches to keyword experiment list and remove them from all experiment list (as
        //the experiment will already be displayed in search so multiple matches are not of interest)
        ArrayList<Experiment> typeKeywordExperiments = this.searchType(keywordList, allExperiments);
        for (int i = 0; i < typeKeywordExperiments.size(); i++) {
            //True for all below: checks that the Experiment is not already part of the keyword list
            //before it is added
            if (!keywordExperiments.contains(typeKeywordExperiments.get(i))) {
                keywordExperiments.add(typeKeywordExperiments.get(i));
            }
        }

        Log.d("TYPE_MATCH", String.valueOf(keywordExperiments));

        //The same as with type, but with experiment name matches
        ArrayList<Experiment> experimentNameKeywordExperiments = this.searchExperimentName(keywordList, allExperiments);
        for (int i = 0; i < experimentNameKeywordExperiments.size(); i++) {
            if (!keywordExperiments.contains(experimentNameKeywordExperiments.get(i))) {
                keywordExperiments.add(experimentNameKeywordExperiments.get(i));
            }
        }

        Log.d("TITLE_MATCH", String.valueOf(keywordExperiments));
        //The same as with type, but with experiment keyword matches
        ArrayList<Experiment> experimentKeywordsKeywordExperiments = this.searchExperimentKeywords(keywordList, allExperiments);
        for (int i = 0; i < experimentKeywordsKeywordExperiments.size(); i++) {
            if (!keywordExperiments.contains(experimentKeywordsKeywordExperiments.get(i))) {
                keywordExperiments.add(experimentKeywordsKeywordExperiments.get(i));
            }
        }
        Log.d("KEYWORD_EXPERIMENTS", String.valueOf(keywordExperiments));
        return keywordExperiments;
    }

}
