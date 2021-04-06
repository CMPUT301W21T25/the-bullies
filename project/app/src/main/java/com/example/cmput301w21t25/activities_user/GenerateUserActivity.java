package com.example.cmput301w21t25.activities_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.MainActivity;
import com.example.cmput301w21t25.user.User;
import com.example.cmput301w21t25.managers.UserManager;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * this activity is used to create the user
 * @author Curtis Yalmaz
 */
public class GenerateUserActivity extends AppCompatActivity {
    private String userID;

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_generate_user);
        //String userID;
        this.userID = getIntent().getStringExtra("USER_ID");
        //this can be called on click when
        //FB_CreateUser(userID);
        //finish();
    }

    /**
     * method used to define the behaviour of the Create user button
     * it calls FB_Create method to make a new user
     * @param view
     */
    public void createUserButton(View view) {
        EditText name = findViewById(R.id.userName);
        EditText email = findViewById(R.id.userEmail);
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        Toast toast = Toast.makeText(getApplicationContext(), "Please don't leave any blank", Toast.LENGTH_LONG);
        if (userName != "" && userEmail != "") {
            FB_CreateUser(userID, userName, userEmail);
            //launch MainActivity?
            Intent intent = new Intent(GenerateUserActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            toast.show();
        }

    }


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserManager userManager = new UserManager();

    /**
     * This method creates a UserProfile document in the DB with the provided params
     * @param id id of the user
     * @param name name of the user
     * @param email email of the user
     */
    public void FB_CreateUser(String id, String name, String email){
        //write code here that takes in the user data like email and username
        String T_username = "this is a test user";
        String T_email = "test@test.ualberta.ca";
        User user = new User(name, email);
        //userManager.FB_CreateUserProfile(id,T_username,T_email,user);
        userManager.FB_CreateUserProfile(id, name, email, user);
        Log.d("YA-DB: ","user created");
    }

}
