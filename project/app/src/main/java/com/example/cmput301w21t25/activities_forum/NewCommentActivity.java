package com.example.cmput301w21t25.activities_forum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.managers.ForumManager;

public class NewCommentActivity extends AppCompatActivity {
    private Toolbar commentViewHeader;
    private EditText commentEditText;
    private Button confirmCommentButton;

    private String userID;
    private Experiment forumExperiment;

    private String commentBody;

    private ForumManager forumManager = new ForumManager();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_new_comment);

        userID = getIntent().getStringExtra("USER_ID");
        forumExperiment = (Experiment) getIntent().getSerializableExtra("FORUM_EXPERIMENT");

        commentViewHeader = findViewById(R.id.newCommentHeader);
        commentEditText = findViewById(R.id.commentBody);
        confirmCommentButton = findViewById(R.id.commentButton);

        commentViewHeader.setTitle("Comment on " + forumExperiment.getName());
        commentViewHeader.setSubtitle(forumExperiment.getOwner());

        //Get comment body and send info up to database before returning to forum list activity
        confirmCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentBody = commentEditText.getText().toString();
                forumManager.FB_CreateComment(forumExperiment.getFb_id(), commentBody,"NEED_DB_CALL_TO_GET_USERNAME", userID, "", "");

                Intent returnToForum = new Intent(NewCommentActivity.this, ForumActivity.class);
                returnToForum.putExtra("USER_ID", userID);
                returnToForum.putExtra("FORUM_EXPERIMENT", forumExperiment);
                startActivity(returnToForum);
            }
        });
    }
}
