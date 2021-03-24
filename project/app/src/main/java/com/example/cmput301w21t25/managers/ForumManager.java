package com.example.cmput301w21t25.managers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.cmput301w21t25.forum.Comment;

import java.util.ArrayList;
import java.util.Comparator;

public class ForumManager {

    public ForumManager() { }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Comment> nestedComments(ArrayList<Comment> comments) {
        ArrayList<Comment> orderedForum = new ArrayList<Comment>();

        //Sort the comments by date to preserve sensical order
        comments.sort(Comparator.comparing(comment -> comment.getCommentDate()));

        //First add new thread comments that have no parents (were passed empty parentID string
        //upon construction)
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            if (comment.getCommentParent().equals("")) {
                orderedForum.add(comments.remove(i));
            }
        }

        //The outer loop runs while the size of the array of comments passed is greater than zero,
        //as in some instances the parents of children comments will also be children, so they would
        //not be present to check against in the orderedForum list on a first pass
        while (comments.size() > 0) {
            for (int i = 0; i < comments.size(); i++) {
                Comment childComment = comments.get(i);
                //The current child is checked against each existing comment in the orderedForum
                //list for a parent
                checkParent:
                for (int j = 0; j < orderedForum.size(); j++) {
                    Comment parentComment = orderedForum.get(j);
                    //If the parent is found, it is added to the orderedForum list and removed from
                    //the comments list. If not, it will be returned to on the next pass of the
                    // outer loop after new potential parents have been added to the orderedForum list
                    if (parentComment.getCommentID().equals(childComment.getCommentParent())) {
                        //The number of children is incremented so that the child is properly
                        //placed in the list (e.g., if the parent is at index 4 and currently has
                        //2 children, its children are at index 5 and 6. When another child is found,
                        //the children count is incremented and the new child is placed at the parent
                        //index + its children count, 4 + 3, which means it is at index 7, the
                        //properly ordered index
                        parentComment.setCommentChildren(parentComment.getCommentChildren() + 1);
                        orderedForum.add((j + parentComment.getCommentChildren()), comments.remove(i));
                        break checkParent;
                    }
                }
            }
        }

        return orderedForum;
    }
}
