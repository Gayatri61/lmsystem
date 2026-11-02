package com.gt.backup;
public class InstructorBackup {

    private int userId;
    private String specialization;

    public InstructorBackup() {
    }

    public InstructorBackup(
            int userId,
            String specialization) {

        this.userId = userId;
        this.specialization = specialization;
    }

    public int getUserId() {
        return userId;
    }

    public String getSpecialization() {
        return specialization;
    }
}
