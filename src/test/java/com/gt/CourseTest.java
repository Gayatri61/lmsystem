package com.gt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;
import com.gt.model.Course;
import com.gt.model.Instructor;
import com.gt.model.Lesson;

public class CourseTest {

    @Test 
    public void lessonCanBeAddedToCourse() {

        Instructor instructor = new Instructor(
                1,
                "Max Mustermann",
                "max@javatech.de",
                "Java"
        );

    Course course = new Course(
            101,
            "Spring Boot",
            "Webentwicklung mit Java",
            instructor, CourseCategory.WEB_DEVELOPMENT,
            DifficultyLevel.INTERMEDIATE, 99.99
    );

        Lesson lesson = new Lesson(
                1,
                "Variablen",
                "Grundlagen der Java-Variablen"
        );

        course.addLesson(lesson);

        assertEquals(1, course.getLessons().size());
        assertEquals(
                "Variablen",
                course.getLessons().get(0).getTitle()
        );
    }
}
