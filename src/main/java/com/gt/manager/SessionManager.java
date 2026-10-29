package com.gt.manager;

import com.gt.model.Student;

public final class SessionManager {

    private static Student currentStudent;

    private SessionManager() {
    }

    public static void login(Student student) {
        currentStudent = student;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static void logout() {
        currentStudent = null;
    }
}
