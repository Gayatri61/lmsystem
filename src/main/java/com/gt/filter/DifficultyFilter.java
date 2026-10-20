package com.gt.filter;

import com.gt.enums.DifficultyLevel;
import com.gt.model.Course;

public class DifficultyFilter
        implements CourseFilterStrategy {

    private final DifficultyLevel difficultyLevel;

    public DifficultyFilter(
            DifficultyLevel difficultyLevel) {

        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public boolean matches(Course course) {

        return course.getDifficultyLevel()
                == difficultyLevel;
    }

}