package com.example.cmput301w21t25;
import com.example.cmput301w21t25.user.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserTest {
    private User mockUser(){
        User user = new User("TestName", "TestEmail");
        return user;
    }

    @Test
    void testUser(){
        User mockUser = mockUser();
        assertEquals("TestName", mockUser.getName());
        assertEquals("TestEmail", mockUser.getEmail());

        mockUser.setName("NewName");
        mockUser.setEmail("NewEmail");
        assertEquals("NewName", mockUser.getName());
        assertEquals("NewEmail", mockUser.getEmail());
    }
}
