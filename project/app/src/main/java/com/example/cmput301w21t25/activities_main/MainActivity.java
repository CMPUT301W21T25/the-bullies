package com.example.cmput301w21t25.activities_main;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_user.GenerateUserActivity;
import com.example.cmput301w21t25.user.User;
import com.example.cmput301w21t25.managers.UserManager;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.managers.TrialManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    float x1;
    float x2;
    //testing user stuff atm-YA
    UserManager testM;
    User test;
    String email = "Loading";
    ExperimentManager expMtest;
    TrialManager trialManeTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //testing DB ill make proper tests later i promise -YA
        //tests for UserMangaer class and UserProfile collection
//        testM = new UserManager();
//        test = new User();
//        ArrayList<String> testList = new ArrayList<>();
//        testList.add("this");
//        testList.add("is");
//        testList.add("not a");
//        testList.add("test");
//        testM.FB_CreateUserProfile("test1","TestOne","test@test.gmail.com",test);
//        testM.FB_UpdateSubscriptions(testList,"test1");
//        testM.FB_UpdateOwnedExperiments(testList,"test1");
//        testM.FB_UpdateConductedTrials(testList,"test1");

        //test for Experiminet manager
//        expMtest = new ExperimentManager();
//        Location testloc = new Location("edm");

        //expMtest.FB_CreateExperiment("TestName","fdNzWupOTDKvwkrVHMADau", "this is a test",testloc,testList,false,false,"abstract",new Date());
//        expMtest.FB_UpdateDescription("new description","03XLnxuIaI7CW7DnpsMb");
//        expMtest.FB_UpdateGeoEnabled(true,"BIKvOCxENl3ByUtNGmf7");
//        expMtest.FB_UpdatePublished(true,"BIKvOCxENl3ByUtNGmf7");
//        expMtest.FB_UpdateTags(testList,"BIKvOCxENl3ByUtNGmf7");
//        expMtest.FB_UpdateConductedTrials(testList,"BIKvOCxENl3ByUtNGmf7");

        //test for Trial managerTest
        trialManeTest = new TrialManager();
        //trialManeTest.FB_CreateCountTrial("fdNzWupOTDKvwkrVHMADau","oReMYjJ95nOsNJ3fOSVV","Test","Test",true,20);
        //trialManeTest.FB_UpdateCountResult(30,"VaijIfF3nVz0dbnwOI4i");
        getLaunchInfo();

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
                    //Intent switchScreen = new Intent(MainActivity.this, TempRightActivity.class);
                    //startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }



    /********************************************
     * DB Functions HERE!!!!!!!!!!!!!!!!!!!!!!!!!
     ********************************************
     *******************************************/
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private void getLaunchInfo(){
        /**
         * this function retrives launch critical information from the database and starts the next activities accordingly
         * it first finds the the Firebase Installation ID which is used to identify the device
         * it then checks the database to see if there is a user profile associated with the FID
         * if such a profile exists it retrives its id and passes the FID onto the HomeSubbedActivity via Intent
         * if such a profile doesn't exist it passes the FID to GenerateUserActivity via intent
         * @param none
         * @return void
         * */
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        //this first query on completion gets the userID which is the device's firebase install ID
                        if (task.isSuccessful()) {
                            Log.d("YA-Installations", "Installation ID: " + task.getResult());
                            String userID = task.getResult();
                            DocumentReference docRef = db.collection("UserProfile").document(userID);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("YA-DB:", "User document retrieved passing ID to MyUserProfileActivity");
                                            //EDEN:
                                            //For list testing I'm going to send it to homeOwned instead
                                            //Can return to userProfile activity later
                                            Intent intent = new Intent(getBaseContext(), HomeOwnedActivity.class);
                                            intent.putExtra("USER_ID", userID);
                                            startActivity(intent);
                                            //start sublist activity
                                        } else {
                                            Log.d("YA-DB:", "No document associated with ID passing to GenerateUserAcitivty");
                                            //document associated with user does not exist start GenerateUser_activity
                                            Intent intent = new Intent(getBaseContext(), GenerateUserActivity.class);
                                            intent.putExtra("USER_ID", userID);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Log.d("YA-DB:", "User Profile Query Failed", task.getException());
                                        //this means it couldnt complete query
                                    }
                                }
                            });
                        } else {
                            Log.e("YA-DB", "Unable to get Installation ID", task.getException());
                            //this means user device could not be identified setup fail state
                        }
                    }
                });
    }

}