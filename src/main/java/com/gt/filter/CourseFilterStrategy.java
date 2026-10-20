package com.gt.filter;

import com.gt.model.Course;

public interface CourseFilterStrategy {

    boolean matches(Course course);
}
