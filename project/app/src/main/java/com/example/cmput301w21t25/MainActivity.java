package com.example.cmput301w21t25;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    float x1;
    float x2;
    //testing user stuff atm-YA
    UserManager testM;
    User test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testing DB ill make proper tests later i promise -YA
        testM = new UserManager();
        test = new User();
        //testM.FB_AddUser(test);
        User test2 = testM.FB_GetUser("kRSx5awciCOnX7SkAnJj");
        Log.d("output: ",test.getUserName());
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