package com.example.cmput301w21t25.activities_forum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.managers.ForumManager;

public class NewReplyActivity extends AppCompatActivity {
    private Toolbar replyViewHeader;
    private TextView originalMessageView;
    private EditText replyEditText;
    private Button confirmReplyButton;

    private String userID;
    private Experiment forumExperiment;
    private Comment replyingToComment;

    private String replyBody;

    private ForumManager forumManager = new ForumManager();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_new_reply);

        userID = getIntent().getStringExtra("USER_ID");
        forumExperiment = (Experiment) getIntent().getSerializableExtra("FORUM_EXPERIMENT");
        replyingToComment = (Comment) getIntent().getSerializableExtra("IN_RESPONSE_TO");

        replyViewHeader = findViewById(R.id.newReplyHeader);
        originalMessageView = findViewById(R.id.originalMessage);
        replyEditText = findViewById(R.id.replyBody);
        confirmReplyButton = findViewById(R.id.replyButton);

        replyViewHeader.setTitle("Reply to " + replyingToComment.getCommenterName());
        replyViewHeader.setSubtitle("On " + forumExperiment.getName());
        originalMessageView.setText("Original comment: \"" + replyingToComment.getComment() + "\"");

        //Get reply body and send info up to database before returning to forum list activity
        confirmReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyBody = replyEditText.getText().toString();
                if (replyingToComment.getComment().length() < 15) {
                    forumManager.FB_CreateComment(forumExperiment.getFb_id(), replyBody, userID, replyingToComment.getCommentID(), replyingToComment.getComment());
                }
                else {
                    forumManager.FB_CreateComment(forumExperiment.getFb_id(), replyBody, userID, replyingToComment.getCommentID(), replyingToComment.getComment().substring(0, 15));
                }

                Intent returnToForum = new Intent(NewReplyActivity.this, ForumActivity.class);
                returnToForum.putExtra("USER_ID", userID);
                returnToForum.putExtra("FORUM_EXPERIMENT", forumExperiment);
                startActivity(returnToForum);
            }
        });
    }
}
