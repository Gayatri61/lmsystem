package com.gt.manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.gt.exception.LMSException;
import com.gt.filter.CourseFilterStrategy;
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


public List<Course> searchCourses(String searchPattern)
        throws LMSException {

    if (searchPattern == null || searchPattern.isBlank()) {
        throw new LMSException(
                "Der Suchbegriff darf nicht leer sein."
        );
    }

    List<Course> results = new ArrayList<>();

    try {
        Pattern pattern = Pattern.compile(
                searchPattern,
                Pattern.CASE_INSENSITIVE
        );

        for (Course course : courses.values()) {

            boolean foundInTitle =
                    pattern.matcher(
                            course.getTitle()
                    ).find();

            boolean foundInDescription =
                    pattern.matcher(
                            course.getDescription()
                    ).find();

            if (foundInTitle || foundInDescription) {
                results.add(course);
            }
        }

    } catch (PatternSyntaxException e) {

        throw new LMSException(
                "Ungültiger regulärer Ausdruck: "
                        + searchPattern,
                e
        );
    }

    return results;
}

public List<Course> filterCourses(
        CourseFilterStrategy strategy) {

    List<Course> results = new ArrayList<>();

    for (Course course : courses.values()) {

        if (strategy.matches(course)) {
            results.add(course);
        }
    }

    return results;
}

public class CombinedFilter
        implements CourseFilterStrategy {

    private final List<CourseFilterStrategy> strategies;

    public CombinedFilter(
            List<CourseFilterStrategy> strategies) {

        this.strategies = strategies;
    }

    @Override
    public boolean matches(Course course) {

        for (CourseFilterStrategy strategy : strategies) {

            if (!strategy.matches(course)) {
                return false;
            }
        }

        return true;
    }
}


}