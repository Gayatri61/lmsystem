package com.gt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Enrollment {

    private int id;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private Integer rating;
    private List<LessonProgress> lessonProgressList;


    public Enrollment() {
    }

    public Enrollment(int id, Student student, Course course) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.enrollmentDate = LocalDate.now();
        this.lessonProgressList = new ArrayList<>();

        for (Lesson lesson : course.getLessons()) {

            lessonProgressList.add(
                    new LessonProgress(lesson)
            );
        }
        
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

    
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<LessonProgress> getLessonProgressList() {
        return lessonProgressList;
    }

    public void setLessonProgressList(List<LessonProgress> lessonProgressList) {
        this.lessonProgressList = lessonProgressList;
    }
}
