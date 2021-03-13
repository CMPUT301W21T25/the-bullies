package com.example.cmput301w21t25;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
        setContentView(R.layout.activity_main);
        //testing DB ill make proper tests later i promise -YA
        //tests for UserMangaer class and UserProfile collection
        testM = new UserManager();
        test = new User();
        ArrayList<String> testList = new ArrayList<>();
        testList.add("this");
        testList.add("is");
        testList.add("not a");
        testList.add("test");
        testM.FB_CreateUserProfile("test1","TestOne","test@test.gmail.com",test);
        testM.FB_UpdateSubscriptions(testList,"test1");
        testM.FB_UpdateOwnedExperiments(testList,"test1");
        testM.FB_UpdateConductedTrials(testList,"test1");

        //test for Experiminet manager
        expMtest = new ExperimentManager();
        Location testloc = new Location("edm");
        Experiment experiment = new Experiment();
        //expMtest.FB_CreateExperiment("testExp","test1", "this is a test",testloc,testList,false,false,experiment);
        expMtest.FB_UpdateDescription("new description","BIKvOCxENl3ByUtNGmf7");
        expMtest.FB_UpdateGeoEnabled(true,"BIKvOCxENl3ByUtNGmf7");
        expMtest.FB_UpdatePublished(true,"BIKvOCxENl3ByUtNGmf7");
        expMtest.FB_UpdateTags(testList,"BIKvOCxENl3ByUtNGmf7");
        expMtest.FB_UpdateConductedTrials(testList,"BIKvOCxENl3ByUtNGmf7");
        expMtest.FB_UpdateExperimentClass(experiment,"BIKvOCxENl3ByUtNGmf7");

        //test for Trial managerTest
        trialManeTest = new TrialManager();
        Trial testTrial = new Trial();
        //trialManeTest.FB_CreateTrial("TestDummy","BIKvOCxENl3ByUtNGmf7",testloc,false,false,testTrial);
        trialManeTest.FB_UpdateHidden(true,"13m0s2kkGBkERhcE15V2");
        trialManeTest.FB_UpdatePublished(true,"13m0s2kkGBkERhcE15V2");
        trialManeTest.FB_UpdateTrial(testTrial,"13m0s2kkGBkERhcE15V2");
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
                    Intent switchScreen = new Intent(MainActivity.this, TempRightActivity.class);
                    startActivity(switchScreen);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
        }
        return super.onTouchEvent(event);
    }



    /********************************************
     * Functions HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!
     ********************************************
     *******************************************/
    //SECTION 1:
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userName;
    String userEmail;
    User user;
    ArrayList<String>subscriptionKeys;
    ArrayList<String>trialKeys;
    ArrayList<String>experimentKeys;
    private void getLaunchInfo(){
        //RETRIEVES ALL USER INFORMATION*Construction noises*
        FirebaseInstallations.getInstance().getId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        //this first query on completion gets the userID which is the device's firebase install ID
                        if (task.isSuccessful()) {
                            String userID = task.getResult();
                            Log.d("YA-Installations", "Installation ID: " + task.getResult());
                            //this means data retrieval was successful launch SubList Activity
                        } else {
                            Log.e("Installations", "Unable to get Installation ID");
                            //this means data dosnt exist luanch Generate ID
                        }
                    }
                });
    }

}