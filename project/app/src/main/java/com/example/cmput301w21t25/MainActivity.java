package com.example.cmput301w21t25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    float x1;
    float x2;
    //testing user stuff atm-YA
    UserManager testM;
    User test;
    String email = "Loading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testing DB ill make proper tests later i promise -YA
        //tests for UserMangaer class and UserProfile collection
        testM = new UserManager();
        test = new User();
        ArrayList<String> testList = new ArrayList<>();
        testList.add("this");
        testList.add("is");
        testList.add("a");
        testList.add("test");
        testM.FB_CreateUserProfile("test1","TestOne","test@test.gmail.com",test);
        testM.FB_UpdateSubscriptions(testList,"test1");
        testM.FB_UpdateOwnedExperiments(testList,"test1");
        testM.FB_UpdateConductedTrials(testList,"test1");
        //test for
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 > (x2)) {
                    Intent switchScreen = new Intent(MainActivity.this, TempRightActivity.class);
                    startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}