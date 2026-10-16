package com.gt.model;

import java.time.LocalDate;

public class Enrollment {

    private int id;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private double progress;

    public Enrollment() {
    }

    public Enrollment(int id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.progress = 0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
