package com.gt.model;


public class Instructor extends User {

    private String specialization;

    public Instructor() {
    }

    public Instructor(int id, String name, String email,
                      String specialization) {
        super(id, name, email);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
