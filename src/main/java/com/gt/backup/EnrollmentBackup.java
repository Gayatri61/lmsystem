package com.gt.backup;

import java.time.LocalDate;

/**
 * EnrollmentBackup
 */
public class EnrollmentBackup {
    
    private int id;
    private int studentId;
    private int courseId;
    private LocalDate enrollmentDate;
    private Integer rating;


    public EnrollmentBackup() {
    }

    public EnrollmentBackup(int id, int studentId, int courseId, LocalDate enrollmentDate, Integer rating) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public Integer getRating() {
        return rating;
    }
}
