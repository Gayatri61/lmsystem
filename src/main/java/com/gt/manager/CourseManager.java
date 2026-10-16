package com.gt.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gt.exception.LMSException;
import com.gt.model.Course;

public class CourseManager {

    private static final CourseManager INSTANCE =
            new CourseManager();

    private final Map<Integer, Course> courses;

    private CourseManager() {
        courses = new HashMap<>();
    }

    public static CourseManager getInstance() {
        return INSTANCE;
    }

    public void addCourse(Course course)
            throws LMSException {

        if (course == null) {
            throw new LMSException(
                    "Kurs darf nicht null sein."
            );
        }

        if (courses.containsKey(course.getId())) {
            throw new LMSException(
                    "Kurs mit ID "
                    + course.getId()
                    + " existiert bereits."
            );
        }

        courses.put(course.getId(), course);
    }

    public void removeCourse(int courseId)
            throws LMSException {

        if (!courses.containsKey(courseId)) {
            throw new LMSException(
                    "Kurs mit ID "
                    + courseId
                    + " wurde nicht gefunden."
            );
        }

        courses.remove(courseId);
    }

    public Course findCourseById(int courseId) {
        return courses.get(courseId);
    }

    public Course findCourseByTitle(String title) {

        for (Course course : courses.values()) {

            if (course.getTitle().equalsIgnoreCase(title)) {
                return course;
            }
        }

        return null;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
}