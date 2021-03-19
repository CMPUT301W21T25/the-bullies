package com.example.cmput301w21t25;

import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.SearchManager;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest {

    private Experiment mockExperiment() {
        Experiment mockExperiment = new BinomialExperiment();

        return mockExperiment;
    }

    private SearchManager mockSearchManager() {
        SearchManager mockSearchManager = new SearchManager();

        return mockSearchManager;
    }

    @Test
    void testSearch() {
        ArrayList<Experiment> experiments = new ArrayList<Experiment>();
        SearchManager searchManager = mockSearchManager();
        String userInput1 = "DUNCAN, measurement";
        String userInput2 = "  stupid, butt";
        String userInput3 = "true";
        String userInput4 = "nope, not, match, with, anything";

        //The test assumes that keywords are properly parsed and made all lower case
        //at the time of user input
        //The test also assumes experiment name has been validated-- no special characters,
        //only spaces
        ArrayList<String> keywords1 = new ArrayList<String>();
        keywords1.add("butt");
        keywords1.add("stupid");
        keywords1.add("funny");

        ArrayList<String> keywords2 = new ArrayList<String>();
        keywords2.add("cats");
        keywords2.add("dogs");

        Experiment experiment1 = mockExperiment();
        experiment1.setName("Duncan is a Dingus");
        experiment1.setTags(keywords1);
        experiment1.setType("binomial");

        Experiment experiment2 = mockExperiment();
        experiment2.setName("Yup that's True");
        experiment2.setTags(keywords2);
        experiment2.setType("measurement");

        experiments.add(experiment1);
        experiments.add(experiment2);
        //Make copies of array because APPARENTLY JAVA IS PASS BY REFERENCE MY GOD
        ArrayList<Experiment> newExperiments = new ArrayList<Experiment>(experiments);
        ArrayList<Experiment> newExperiments2 = new ArrayList<Experiment>(experiments);
        ArrayList<Experiment> newExperiments3 = new ArrayList<Experiment>(experiments);

        //Test both have key word match (name and type)
        ArrayList<Experiment> keywordExperiments = searchManager.searchExperiments(userInput1, experiments);
        assertTrue(keywordExperiments.contains(experiment1));
        assertTrue(keywordExperiments.contains(experiment2));
        assertEquals(2, keywordExperiments.size());

        //Test 1st has match (keyword) with repeat
        keywordExperiments = searchManager.searchExperiments(userInput2, newExperiments);
        assertTrue(keywordExperiments.contains(experiment1));
        assertEquals(1, keywordExperiments.size());

        //Test 2nd has match (name)
        keywordExperiments = searchManager.searchExperiments(userInput3, newExperiments2);
        assertTrue(keywordExperiments.contains(experiment2));
        assertEquals(1, keywordExperiments.size());

        //Test no matches
        keywordExperiments = searchManager.searchExperiments(userInput4, newExperiments3);
        assertEquals(0, keywordExperiments.size());



    }
}