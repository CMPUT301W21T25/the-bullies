package com.example.cmput301w21t25;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

//For testing purposes
public class TempRightActivity extends AppCompatActivity {

    float x1;
    float x2;
    SearchManager searchManager = new SearchManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_right);

        searchManager.parseKeywords(" cat, dog cat,  ,, BUTTS");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                if (x1 < x2) {
                    Intent switchScreen = new Intent(TempRightActivity.this, MainActivity.class);
                    startActivity(switchScreen);
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}