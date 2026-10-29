package com.gt.observer;

import java.util.Map;

import com.gt.model.Course;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CourseCache implements DataObserver {

    private static CourseCache instance;

    private final Map<Integer, Course> courses =
            new HashMap<>();

    private CourseCache() {
    }

    public static CourseCache getInstance() {

        if (instance == null) {
            instance = new CourseCache();
        }

        return instance;
    }

    public Course get(int id) {
        return courses.get(id);
    }

    public void put(Course course) {
        courses.put(course.getId(), course);
    }

    public void putAll(List<Course> courseList) {

        courses.clear();

        for (Course course : courseList) {
            courses.put(course.getId(), course);
        }
    }

    public List<Course> getAll() {
        return new ArrayList<>(courses.values());
    }

    public boolean isEmpty() {
        return courses.isEmpty();
    }

    public void clear() {
        courses.clear();
    }

    @Override
    public void onDataChanged() {
        clear();
    }
}
