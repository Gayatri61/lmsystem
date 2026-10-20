package com.gt.model;

import java.util.ArrayList;
import java.util.List;

import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;

public class Course {

    private int id;
    private String title;
    private String description;
    private Instructor instructor;
    private List<Lesson> lessons;
    private CourseCategory category;
    private DifficultyLevel difficultyLevel;
    private double price;

    public Course() {
        this.lessons = new ArrayList<>();
    }


    public Course(int id, String title, String description,
                  Instructor instructor, CourseCategory category,
                  DifficultyLevel difficultyLevel, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructor = instructor;
        this.lessons = new ArrayList<>();
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.price = price;
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

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
