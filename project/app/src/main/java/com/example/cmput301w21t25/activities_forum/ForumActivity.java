package com.example.cmput301w21t25.activities_forum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_experiments.ViewCreatedExperimentActivity;
import com.example.cmput301w21t25.custom.CustomListComment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.managers.ForumManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ForumActivity extends AppCompatActivity {

    ListView forumListView;
    FloatingActionButton askQuestionButton;

    private ArrayList<Comment> comments = new ArrayList<Comment>();
    //private ArrayList<Comment> nestedComments;

    private String userID;
    private Experiment forumExperiment;

    private ForumManager forumManager = new ForumManager();
    private ArrayAdapter<Comment> commentArrayAdapter;

    //@RequiresApi(api = Build.VERSION_CODES.N)

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_forum);

        userID = getIntent().getStringExtra("USER_ID");
        forumExperiment = (Experiment) getIntent().getSerializableExtra("FORUM_EXPERIMENT");

        //nestedComments = forumManager.nestedComments(comments);

        forumListView = findViewById(R.id.forum_list);
        askQuestionButton = findViewById(R.id.add_comment_button);

        commentArrayAdapter = new CustomListComment(this, comments, forumExperiment, userID);
        forumListView.setAdapter(commentArrayAdapter);
        //forumManager.FB_CreateComment(forumExperiment.getFb_id(),"this is a test","need to pass name with intent",userID,"","");
        forumManager.FB_FetchComments(forumExperiment,commentArrayAdapter,comments);//<-------THIS TAKES IN THE EXPERIMENT,ADAPTER AND LIST THEN UPDATES THEM FOR U
        Log.d("EDEN'S_OTHER_TEST", String.valueOf(comments));
        comments = forumManager.nestedComments(comments);
        commentArrayAdapter.notifyDataSetChanged();

        askQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newComment = new Intent(ForumActivity.this, NewCommentActivity.class);
                newComment.putExtra("USER_ID", userID);
                newComment.putExtra("FORUM_EXPERIMENT", forumExperiment);
                startActivity(newComment);
            }
        });

        forumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent startNewReply = new Intent(ForumActivity.this, NewReplyActivity.class);
                startNewReply.putExtra("USER_ID", userID);
                startNewReply.putExtra("FORUM_EXPERIMENT", forumExperiment);
                startNewReply.putExtra("IN_RESPONSE_TO", comments.get(position));
                startActivity(startNewReply);
            }
        });

    }
}
