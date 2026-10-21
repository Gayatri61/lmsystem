package com.gt;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;
import com.gt.exception.LMSException;
import com.gt.manager.CourseManager;
import com.gt.model.Course;
import com.gt.model.Instructor;

public class CourseManagerTest {

@Test
public void shouldFindCourseByTitleUsingRegex()
        throws LMSException {

    CourseManager manager =
            CourseManager.getInstance();

    Instructor instructor = new Instructor(
            1,
            "Max",
            "max@test.de",
            "Programming"
    );

    Course course = new Course(
            100,
            "Java Grundlagen",
            "Einführung in Java",
            instructor, CourseCategory.PROGRAMMING, DifficultyLevel.BEGINNER, 100.0
    );

    manager.addCourse(course);

    List<Course> results =
            manager.searchCourses("^Java");

    assertTrue(
            results.stream()
                    .anyMatch(c -> c.getId() == 100)
    );
}   

@Test
public void shouldFindCourseByDescription()
        throws LMSException {

    CourseManager manager =
            CourseManager.getInstance();

    Instructor instructor = new Instructor(
            1,
            "Max",
            "max@test.de",
            "Programming"
    );

    Course course = new Course(
            101,
            "Spring Boot",
            "Webentwicklung mit Java",
            instructor, CourseCategory.WEB_DEVELOPMENT,
            DifficultyLevel.INTERMEDIATE, 99.99
    );

    manager.addCourse(course);

    List<Course> results =
            manager.searchCourses("Java");

    assertTrue(
            results.stream()
                    .anyMatch(c -> c.getId() == 101)
    );
}
    
}
