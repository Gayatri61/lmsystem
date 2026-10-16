package com.gt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gt.model.Course;
import com.gt.model.Instructor;
import com.gt.model.Lesson;

class CourseTest {

    @Test 
    void lessonCanBeAddedToCourse() {

        Instructor instructor = new Instructor(
                1,
                "Max Mustermann",
                "max@javatech.de",
                "Java"
        );

        Course course = new Course(
                1,
                "Java Grundlagen",
                "Einführung in Java",
                instructor
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
