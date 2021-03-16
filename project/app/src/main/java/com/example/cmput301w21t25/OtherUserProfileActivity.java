package com.example.cmput301w21t25;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OtherUserProfileActivity extends AppCompatActivity {
    //attributes
    private String Username;
    private String ContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile_view);
    }



    public void backButton(View view) {

    }
}
