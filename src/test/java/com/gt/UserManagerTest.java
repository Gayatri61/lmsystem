package com.gt;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.gt.exception.LMSException;
import com.gt.manager.UserManager;
import com.gt.model.Student;
import com.gt.model.User;

public class UserManagerTest {

    @Test
    public void userCanBeAdded() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                0,
                "Anna",
                "anna_add_test@test.de"
        );

        // Add user
        manager.addUser(student);

        // Retrieve generated ID
        int generatedId = student.getId();

        // Find user in database
        User result = manager.findUserById(generatedId);

        assertNotNull(result);
        assertEquals("Anna", result.getName());
        assertEquals("anna_add_test@test.de", result.getEmail());

        // Cleanup
        manager.removeUser(generatedId);
    }

    @Test
    public void userCanBeRemoved() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                0,
                "Max",
                "max_remove_test@test.de"
        );

        // Add user
        manager.addUser(student);

        int generatedId = student.getId();

        // Verify user exists
        assertNotNull(manager.findUserById(generatedId));

        // Remove user
        manager.removeUser(generatedId);

        // Verify user no longer exists
        User result = manager.findUserById(generatedId);

        assertNull(result);
    }

    @Test
    public void userCanBeFoundByEmail() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                0,
                "Lisa",
                "lisa_find_test@test.de"
        );

        // Add user
        manager.addUser(student);

        // Find user by email
        User result = manager.findUserByEmail("lisa_find_test@test.de");

        assertNotNull(result);
        assertEquals("Lisa", result.getName());
        assertEquals("lisa_find_test@test.de", result.getEmail());

        // Cleanup
        manager.removeUser(result.getId());
    }
}