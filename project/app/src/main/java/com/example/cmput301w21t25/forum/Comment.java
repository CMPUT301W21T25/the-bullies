package com.example.cmput301w21t25.forum;

import java.io.Serializable;
import java.util.Date;

/**
 * The comment class. Comments will be housed in a collection "Forum" held by each individual
 * experiment. The only value that won't be stored in the database is commentChildren, as is used
 * to organize the ListView
 */
public class Comment implements Serializable {

    private String comment;
    private String commentID;
    private String commenterName;
    private String commenterID;
    private String commentParent = "";
    private String respondingTo = "";

    private Integer commentChildren = 0;
    private Integer commentDepth = 0;

    private Date commentDate;

    public Comment() {}

    //The constructor used when making a new comment
    public Comment(String comment, String commentID, String commenterName, String commenterID, Date commentDate) {
        this.comment = comment;
        this.commentID = commentID;
        this.commenterName = commenterName;
        this.commenterID = commenterID;
        this.commentDate = commentDate;
    }

    //The constructor used when replying to a comment
    public Comment(String comment, String commentID, String commenterName, String commenterID, String commentParent, String respondingTo, Date commentDate) {
        this.comment = comment;
        this.commentID = commentID;
        this.commenterName = commenterName;
        this.commenterID = commenterID;
        this.commentParent = commentParent;
        this.respondingTo = respondingTo;
        this.commentDate = commentDate;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public String getCommenterID() {
        return commenterID;
    }

    public String getCommentParent() {
        return commentParent;
    }

    public String getRespondingTo() {
        return respondingTo;
    }

    public Integer getCommentChildren() {
        return commentChildren;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public Integer getCommentDepth() {return commentDepth; }

    //The number of children (responses) a comment has will be needed when ordering a list
    public void setCommentChildren(Integer commentChildren) {
        this.commentChildren = commentChildren;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setCommentDepth(Integer commentDepth) {this.commentDepth = commentDepth; }
}
