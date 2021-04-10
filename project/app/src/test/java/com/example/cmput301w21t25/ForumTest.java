package com.example.cmput301w21t25;

import com.example.cmput301w21t25.forum.Comment;
import com.example.cmput301w21t25.managers.ForumManager;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertSame;


public class ForumTest {
    //This date is global for test it the date set right
    Date date = new Date();
    //Set up the mock comment
    private Comment mockComment(){
        Comment comment = new Comment("This is comment","c1","tester","tester123", date);
        return comment;
    }
    //Set up the mock reply
    private Comment mockReply(){
        Comment reply = new Comment("Reply to a comment","c1","replyer","replyer123","parent","respondingTo", date);
        return reply;
    }
    //Test is the comment had set up properly
    @Test
    void testComment(){
        Comment comment = mockComment();
        Comment reply = mockReply();

        assertEquals("This is comment",comment.getComment());
        assertEquals("Reply to a comment",reply.getComment());
    }
    //Test if the comment id set up properly
    @Test
    void testCommentId(){
        Comment comment = mockComment();
        Comment reply = mockReply();

        assertEquals("c1",comment.getCommentID());
        assertEquals("c1",reply.getCommentID());

        comment.setCommentID("c12");
        reply.setCommentID("c13");
        assertEquals("c12",comment.getCommentID());
        assertEquals("c13",reply.getCommentID());
    }
    //Test if the commenter name set up properly
    @Test
    void testCommenterName(){
        Comment comment = mockComment();
        Comment reply = mockReply();

        assertEquals("tester",comment.getCommenterName());
        assertEquals("replyer",reply.getCommenterName());
    }
    //Test if the commenter id set up properly
    @Test
    void testCommenterId(){
        Comment comment = mockComment();
        Comment reply = mockReply();

        assertEquals("tester123",comment.getCommenterID());
        assertEquals("replyer123",reply.getCommenterID());
    }
    //Test if the date set up properly for comment
    @Test
    void testDate(){
        Comment comment = mockComment();
        Comment reply = mockReply();

        assertNotEquals(0,comment.getCommentDate());
        assertNotEquals(0,reply.getCommentDate());
        assertEquals(date,comment.getCommentDate());
        assertEquals(date,reply.getCommentDate());
    }
    //Test if comment parent set up properly
    @Test
    void testCommentParent(){
        Comment reply = mockReply();

        assertEquals("parent",reply.getCommentParent());
    }
    //Test if the responding to set up properly
    @Test
    void testRespondingTo(){
        Comment reply = mockReply();

        assertEquals("respondingTo",reply.getRespondingTo());
    }

}