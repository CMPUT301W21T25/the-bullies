package com.example.cmput301w21t25;

import android.location.Location;

import androidx.annotation.NonNull;

import com.example.cmput301w21t25.experiments.BinomialExperiment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ExperimentManager;
import com.example.cmput301w21t25.trials.Trial;
import com.example.cmput301w21t25.trials.BinomialTrial;
import com.example.cmput301w21t25.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExperimentManagerTest{

//    User user1 = new User();
//    User user2 = new User();
//    User user3 = new User();

    private ExperimentManager mockExperimentManager() {
        ExperimentManager mockManager = new ExperimentManager();
        return mockManager;
    }

    @Test
    void testCreateExperiment() {
        ExperimentManager mockManager = mockExperimentManager();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Date date = new Date();
        Location region = new Location("TestLocation");
        ArrayList<String> tags = new ArrayList<String>();
        mockManager.FB_CreateExperiment(
                "fdNzWupOTDKvwkrVHMADau",
                "NameOfTest",
                "TestOwner",
                "TestDescription", region, tags,
                false,
                true,
                "Measurement",
                date);

        db.collection("Experiments")
                .whereEqualTo("experimentName","NameOfTest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<Experiment> testList;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot query = task.getResult();
                            testList = query.toObjects(Experiment.class);
                            assertEquals(testList.get(0).getName(), "TestOwner");
                            assertEquals(testList.get(0).getDescription(), "TestDescription");
                        }
                    }
                });
    }

    void testUpdateOwner() {
        ExperimentManager mockManager = mockExperimentManager();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Date date = new Date();
        Location region = new Location("TestLocation");
        ArrayList<String> tags = new ArrayList<String>();
        mockManager.FB_CreateExperiment(
                "fdNzWupOTDKvwkrVHMADau",
                "NameOfTest",
                "TestOwner",
                "TestDescription", region, tags,
                false,
                true,
                "Measurement",
                date);

        db.collection("Experiments")
                .whereEqualTo("experimentName","NameOfTest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<Experiment> testList;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot query = task.getResult();
                            testList = query.toObjects(Experiment.class);
                            assertEquals(testList.get(0).getOwner(), "TestOwner");
                            assertEquals(testList.get(0).getFb_id(), "fdNzWupOTDKvwkrVHMADau");
                        }
                    }
                });
    }
//
//    @Test
//    void testDeleteTrial() {
//        Experiment exp = mockExperiment();
//
//        assertEquals(3, exp.getTrials().size());
//
//        exp.deleteTrial(exp.getTrials().get(0));
//
//        assertEquals(2, exp.getTrials().size());
//    }
//
//    @Test
//    void testHideTrials() {
//        Experiment exp = mockExperiment();
//        assertEquals(0, exp.getHiddenTrials().size());
//
//        exp.hideTrials(user1);
//
//        for (Trial trial: exp.getTrials()) {
//            assertFalse(trial.getUser() == user1);
//        }
//
//        for (Trial trial: exp.getHiddenTrials()) {
//            assertTrue(trial.getUser() == user1);
//        }
//
//        assertEquals(1, exp.getHiddenTrials().size());
//        assertEquals(2, exp.getTrials().size());
//        //test if given user is not present
//        //test if trials are already hidden
//
//        //exp.hideTrials(user3);
//
//
//    }
//
//    @Test
//    void testShowTrials() {
//        Experiment exp = mockExperiment();
//        exp.hideTrials(user2);
//        assertEquals(1, exp.getTrials().size());
//
//        for (Trial trial: exp.getTrials()) {
//            assertTrue(trial.getUser() == user1);
//        }
//
//        exp.showTrials(user2);
//        assertEquals(3, exp.getTrials().size());
//        assertEquals(0, exp.getHiddenTrials().size());
//
//        //test if given user is not present
//        //test if trials are already shown
//
//        exp.showTrials(user3);
//    }
//
//    @Test
//    void testShowAllTrials() {
//        Experiment exp = mockExperiment();
//        exp.hideTrials(user1);
//        exp.hideTrials(user2);
//
//        assertEquals(0, exp.getTrials().size());
//
//        exp.showAllTrials();
//
//        assertEquals(3, exp.getTrials().size());
//
//        //test if trials are already shown
//    }



}
