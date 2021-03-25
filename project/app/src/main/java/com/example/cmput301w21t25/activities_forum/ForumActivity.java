package com.example.cmput301w21t25.activities_forum;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ForumActivity extends AppCompatActivity {

    ListView forumList;
    FloatingActionButton askQuestionButton;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_forum);
    }
}
