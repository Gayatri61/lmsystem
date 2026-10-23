package com.gt.model;

import com.gt.enums.UserType;

public class Instructor extends User {

    private String specialization;

    public Instructor() {
    }

    public Instructor(int id, String name, String email,
                      String specialization) {
        super(id, name, email, UserType.INSTRUCTOR);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
