package com.example.cmput301w21t25;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class GenerateUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_generate_user);
        String userID;
        userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        FB_CreateUser(userID);
        finish();
    }
    /********************************************
    *            DB Functions HERE             *
    ********************************************
    *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserManager userManager = new UserManager();
    public void FB_CreateUser(String id){
        //write code here that takes in the user data like email and username
        String T_username = "this is a test user";
        String T_email = "test@test.ualberta.ca";
        User user = new User();
        userManager.FB_CreateUserProfile(id,T_username,T_email,user);
        Log.d("YA-DB: ","user created");
    }

}
