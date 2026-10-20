package com.gt.filter;

import com.gt.enums.CourseCategory;
import com.gt.model.Course;

public class CategoryFilter
        implements CourseFilterStrategy {

    private final CourseCategory category;

    public CategoryFilter(CourseCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(Course course) {
        return course.getCategory() == category;
    }
}
