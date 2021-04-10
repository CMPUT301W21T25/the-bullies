package com.example.cmput301w21t25.activities_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.MainActivity;
import com.example.cmput301w21t25.activities_trials.ConductBinomialTrialActivity;
import com.example.cmput301w21t25.user.User;
import com.example.cmput301w21t25.managers.UserManager;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This activity is used to create the user
 * @author Curtis Yalmaz
 */
public class GenerateUserActivity extends AppCompatActivity {
    private String userID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserManager userManager = new UserManager();

    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_generate_user);
        this.userID = getIntent().getStringExtra("USER_ID");

        Button done = findViewById(R.id.makeUserButton);
        Button skip = findViewById(R.id.skipProfileCreationButton);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCreation();
            }
        });

    }

    /**
     * The skip button actually creates a new user, but with an empty name and email
     */
    public void skipCreation() {
        EditText name = findViewById(R.id.userName);
        EditText email = findViewById(R.id.userEmail);
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();

        userManager.FB_isUnique(userName, userID, userEmail, this,  "skip");

    }

    /**
     * Create a new user.
     * Appropriate validation in place to ensure fields are not empty
     */
    public void createUser() {
        EditText name = findViewById(R.id.userName);
        EditText email = findViewById(R.id.userEmail);
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();

        if (!userName.equals("") && !userEmail.equals("")) {
            userManager.FB_isUnique(userName, userID, userEmail, this,  "create");
        }
        else {
            //something is null
            Toast.makeText(GenerateUserActivity.this, "Don't leave fields empty!", Toast.LENGTH_SHORT).show();

        }

    }

}
