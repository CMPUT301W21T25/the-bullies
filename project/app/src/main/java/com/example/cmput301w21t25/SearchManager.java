package com.example.cmput301w21t25;

import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SearchManager {

    public ArrayList<String> parseKeywords(String keywords) {

        ArrayList<String> keywordList = new ArrayList<String>();
        StringTokenizer splitKeywords = new StringTokenizer(keywords, ",");

        while (splitKeywords.hasMoreTokens()) {
            String keyword = (String) splitKeywords.nextToken();

            if (keyword.trim().length() > 0) {
                keywordList.add(keyword.trim());
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
}
