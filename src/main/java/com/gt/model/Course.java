package com.gt.model;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private int id;
    private String title;
    private String description;
    private Instructor instructor;
    private List<Lesson> lessons;

    public Course() {
        this.lessons = new ArrayList<>();
    }

    public Course(int id, String title, String description,
                  Instructor instructor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.lessons = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }
}
