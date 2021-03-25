package com.example.cmput301w21t25;

import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.managers.ForumManager;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Covers 100% of lines in forum manager
 */
public class ForumTest {

    private Comment mockNewComment(String comment, String commentID, String commenterName, String commenterID, Date date) {
        Comment mockNewComment = new Comment(comment, commentID, commenterName, commenterID, date);

        return mockNewComment;
    }

    private Comment mockReplyComment(String comment, String commentID, String commenterName, String commenterID, String commentParent, String respondingTO, Date date) {
        Comment mockReplyComment = new Comment(comment, commentID, commenterName, commenterID, commentParent, respondingTO, date);


        return mockReplyComment;
    }

    private ForumManager mockForumManger() {
        ForumManager mockForumManger = new ForumManager();

        return mockForumManger;
    }

    @Test
    void testSort() {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        ForumManager forumManager = mockForumManger();

        Comment newThreadComment = mockNewComment("This is a new thread comment", "firstCommentID", "First Commenter", "firstCommenterID", new Date(2021, 2, 12));
        Comment newReplyComment = mockReplyComment("This is a reply to the first", "secondCommentID", "Second Commenter", "secondCommenterID", "firstCommentID", "First Commenter", new Date(2021, 2, 13));
        Comment newThreadComment2 = mockNewComment("This is a new thread comment", "thirdCommentID", "Third Commenter", "thirdCommenterID", new Date(2021, 2, 14));
        Comment newReplyComment2 = mockReplyComment("This is a reply to the second", "fourthCommentID", "Fourth Commenter", "fourthCommenterID", "thirdCommentID", "Third Commenter", new Date(2021, 2, 15));
        Comment newReplyComment3 = mockReplyComment("This is a reply to a reply", "fifthCommentID", "Fifth Commenter", "fifthCommenterID", "fourthCommentID", "Fourth Commenter", new Date(2021, 2, 16));
        Comment newReplyComment4 = mockReplyComment("This is a reply to the first reply", "sixthCommentID", "Sixth Commenter", "sixthCommenterID", "secondCommentID", "Second Commenter", new Date(2021, 2, 17));

        //add them in reverse order to check date sorting
        comments.add(newReplyComment4);
        comments.add(newReplyComment3);
        comments.add(newReplyComment2);
        comments.add(newThreadComment2);
        comments.add(newReplyComment);
        comments.add(newThreadComment);

        ArrayList<Comment> nestedComments = forumManager.nestedComments(comments);

        assertSame(nestedComments.get(0), newThreadComment);
        assertSame(nestedComments.get(1), newReplyComment);
        assertSame(nestedComments.get(2), newReplyComment4);
        assertSame(nestedComments.get(3), newThreadComment2);
        assertSame(nestedComments.get(4), newReplyComment2);
        assertSame(nestedComments.get(5), newReplyComment3);

        /*//Test both have key word match (name and type)
        ArrayList<Experiment> keywordExperiments = searchManager.searchExperiments(userInput1, experiments);
        assertTrue(keywordExperiments.contains(experiment1));
        assertTrue(keywordExperiments.contains(experiment2));
        assertEquals(2, keywordExperiments.size());

        //Test 1st has match (keyword) with repeat
        keywordExperiments = searchManager.searchExperiments(userInput2, newExperiments);
        assertTrue(keywordExperiments.contains(experiment1));
        assertEquals(1, keywordExperiments.size());

        //Test 2nd has match (name)
        keywordExperiments = searchManager.searchExperiments(userInput3, newExperiments2);
        assertTrue(keywordExperiments.contains(experiment2));
        assertEquals(1, keywordExperiments.size());

        //Test no matches
        keywordExperiments = searchManager.searchExperiments(userInput4, newExperiments3);
        assertEquals(0, keywordExperiments.size());*/



    }
}