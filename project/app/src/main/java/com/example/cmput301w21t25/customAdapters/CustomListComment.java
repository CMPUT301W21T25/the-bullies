package com.example.cmput301w21t25.customAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmput301w21t25.R;
import com.example.cmput301w21t25.experiments.Experiment;
import com.example.cmput301w21t25.forum.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Eden
 * A custom array adapter used to display comments in list views
 */
public class CustomListComment extends ArrayAdapter<Comment> {
    private ArrayList<Comment> comments;
    private Context context;
    private Experiment forumExperiment;
    private Comment comment;
    private Date date;
    private String dateString;

    public CustomListComment(Context context, ArrayList<Comment> comments, Experiment forumExperiment) {
        super(context,0,comments);
        this.comments = comments;
        this.context = context;
        this.forumExperiment = forumExperiment;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        //The content view is not displayed if the adapter is empty
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_content, parent, false);
        }

        comment = comments.get(position);

        ImageView replyGraphic = view.findViewById(R.id.replyToGraphic);
        ImageView newThreadGraphic = view.findViewById(R.id.newThreadGraphic);

        TextView commentHeader = view.findViewById(R.id.replyToText);
        TextView commenterName = view.findViewById(R.id.commenterName);
        TextView commentDate = view.findViewById(R.id.commentDate);
        TextView commentContent = view.findViewById(R.id.comment);

        //ImageButton replyButton = view.findViewById(R.id.replyButton);

        date = comment.getCommentDate();
        dateString = formatDate(date);

        //Set a header/graphic based on whether it's a new comment or a response
        if (comment.getCommentParent().equals("")) {
            newThreadGraphic.setVisibility(View.VISIBLE);
            replyGraphic.setVisibility(View.GONE);
            commentHeader.setText("New Thread");
        }
        else {
            replyGraphic.setVisibility(View.VISIBLE);
            newThreadGraphic.setVisibility(View.GONE);
            commentHeader.setText("Replying to: " + comment.getRespondingTo() + "...");
        }

        //If the commenter is the experiment owner, change the colour of their name and specify
        if (forumExperiment.getOwnerID().equals(comment.getCommenterID())) {
            commenterName.setText("Owner: " + comment.getCommenterName());
            commenterName.setTextColor(context.getResources().getColor(R.color.custom_Yellow_dark));
        }
        else if (!forumExperiment.getOwnerID().equals(comment.getCommentID())) {
            commenterName.setText(comment.getCommenterName());
            commenterName.setTextColor(context.getResources().getColor(R.color.custom_Blue_light));
        }

        //Convert Date object to simple format
        commentDate.setText(dateString);
        commentContent.setText(comment.getComment());
/*
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start reply to comment activity
                Intent startNewReply = new Intent(context, NewReplyActivity.class);
                startNewReply.putExtra("USER_ID", userID);
                startNewReply.putExtra("FORUM_EXPERIMENT", forumExperiment);
                startNewReply.putExtra("IN_RESPONSE_TO", comment);
                context.startActivity(startNewReply);
            }
        });

 */

        return view;
    }

    /**
     *
     * @param date
     * The date the comment was created
     * @return
     * A formatted version of the date (String)
     */
    private String formatDate(Date date) {

        SimpleDateFormat condensedDate = new SimpleDateFormat("MM-dd-yyyy");
        String formattedDate = condensedDate.format(date);
        return formattedDate;
    }

}