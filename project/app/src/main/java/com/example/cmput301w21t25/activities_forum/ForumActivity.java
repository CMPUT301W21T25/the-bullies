package com.example.cmput301w21t25.activities_forum;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.activities_main.CreatedExperimentsActivity;
import com.example.cmput301w21t25.activities_trials.HideTrialActivity;
import com.example.cmput301w21t25.activities_user.MyUserProfileActivity;
import com.example.cmput301w21t25.customAdapters.CustomListComment;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.managers.ForumManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A list view where users can browse questions and replies to question regarding an experiment.
 * From this activity, they can respond to a question, as well ask their own.
 */
public class ForumActivity extends AppCompatActivity {

    ListView forumListView;
    FloatingActionButton askQuestionButton;

    private ArrayList<Comment> comments = new ArrayList<Comment>();

    private String userID;
    private Experiment forumExperiment;

    private ForumManager forumManager = new ForumManager();
    private ArrayAdapter<Comment> commentArrayAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle passedData) {
        super.onCreate(passedData);
        setContentView(R.layout.activity_view_forum);

        //setup the custom toolbar!
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userID = getIntent().getStringExtra("USER_ID");
        forumExperiment = (Experiment) getIntent().getSerializableExtra("FORUM_EXPERIMENT");

        forumListView = findViewById(R.id.forum_list);
        askQuestionButton = findViewById(R.id.add_comment_button);

        commentArrayAdapter = new CustomListComment(this, comments, forumExperiment);
        forumListView.setAdapter(commentArrayAdapter);

        forumManager.FB_FetchComments(forumExperiment,commentArrayAdapter,comments);//<-------THIS TAKES IN THE EXPERIMENT,ADAPTER AND LIST THEN UPDATES THEM FOR U

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

    /**
     * This event is menu setup!
     * @param menu this is the menu being integrated
     * @return true to indicate there is a menu (return false to turn off)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    /**
     * This event is for menu item setup
     * @param item these are items that will be added to the menu
     * @return @return true to indicate there is this item (return false to turn off)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home_button:
                Intent home = new Intent(ForumActivity.this, CreatedExperimentsActivity.class);
                home.putExtra("USER_ID", userID);
                startActivity(home);
                return true;
            case R.id.settings_button:
                Intent user_settings = new Intent(ForumActivity.this, MyUserProfileActivity.class);
                user_settings.putExtra("USER_ID", userID);
                //I think this will work but have to check
                user_settings.putExtra("TRIAL_PARENT", forumExperiment);
                user_settings.putExtra("prevScreen", "Forum");
                startActivity(user_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
