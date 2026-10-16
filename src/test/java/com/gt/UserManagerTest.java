package com.gt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gt.exception.LMSException;
import com.gt.manager.UserManager;
import com.gt.model.Student;
import com.gt.model.User;

class UserManagerTest {

    @Test 
    void userCanBeAdded() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);

        assertEquals(1, manager.getAllUsers().size());
    }

    @Test
    void userCanBeFoundById() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);

        User result = manager.findUserById(1);

        assertNotNull(result);
        assertEquals("Anna", result.getName());
    }

    @Test
    void userCanBeFoundByEmail() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);

        User result =
                manager.findUserByEmail("anna@test.de");

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void userCanBeRemoved() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);
        manager.removeUser(1);

        assertTrue(manager.getAllUsers().isEmpty());
    }

   
}