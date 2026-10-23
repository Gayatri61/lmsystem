package com.gt;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import com.gt.exception.LMSException;
import com.gt.manager.UserManager;
import com.gt.model.Student;


public class UserManagerTest {

    @Test 
    public void userCanBeAdded() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);

        assertEquals(1, manager.getAllUsers().size());
    }

   /* @Test
    public void userCanBeFoundById() throws LMSException {

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
    public void userCanBeFoundByEmail() throws LMSException {

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
    public void userCanBeRemoved() throws LMSException {

        UserManager manager = UserManager.getInstance();

        Student student = new Student(
                1,
                "Anna",
                "anna@test.de"
        );

        manager.addUser(student);
        manager.removeUser(1);

        assertTrue(manager.getAllUsers().isEmpty());
    }*/

   
}