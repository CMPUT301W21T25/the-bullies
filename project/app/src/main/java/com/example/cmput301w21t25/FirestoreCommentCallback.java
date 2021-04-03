package com.example.cmput301w21t25;

import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;

import java.util.ArrayList;

public interface FirestoreCommentCallback {
    void onCallback(ArrayList<Comment> list);
}
