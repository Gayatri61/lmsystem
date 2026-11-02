package com.gt.backup;

import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;

/**
 * CourseBackup
 */
public class CourseBackup {

    private int id;
    private String title;
    private String description;
    private int instructorId;
    private CourseCategory category;
    private DifficultyLevel difficultyLevel;
    private double price;

    public CourseBackup() {
    }

    public CourseBackup(int id, String title, String description, int instructorId,
                        CourseCategory category, DifficultyLevel difficultyLevel, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.instructorId = instructorId;
        this.category = category;
        this.difficultyLevel = difficultyLevel;
        this.price = price; 
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public double getPrice() {
        return price;
    }
}
