package com.gt.manager;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import com.gt.dao.CourseDAO;
import com.gt.exception.LMSException;
import com.gt.filter.CourseFilterStrategy;
import com.gt.model.Course;

public class CourseManager {

    private static final Logger LOGGER = Logger.getLogger(CourseManager.class.getName());

    private static final CourseManager INSTANCE =null;
            

    private CourseDAO courseDAO;


    private CourseManager() {
        courseDAO = new CourseDAO();
    }

    public static CourseManager getInstance() {
        if (INSTANCE == null) {
            return new CourseManager();
        }

        return INSTANCE;
    }

    public void addCourse(Course course)
            throws LMSException {

        if (course == null) {
            throw new LMSException(
                    "Kurs darf nicht null sein."
            );
        }

        try {
            courseDAO.create(course);
        } catch (SQLException e) {

            LOGGER.warning(
                    "Fehler beim Erstellen des Kurses: "
                            + e.getMessage()
            );
            throw new LMSException(
                    "Fehler beim Erstellen des Kurses.",
                    e
            );
        }
    }

    public void removeCourse(int courseId)
            throws LMSException {
        try {
            courseDAO.delete(courseId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Löschen des Kurses: "
                            + e.getMessage()
            );
            throw new LMSException(
                    "Fehler beim Löschen des Kurses.",
                    e
            );
        }
    }

    public Course findCourseById(int courseId) throws LMSException {
        try {
            return courseDAO.findById(courseId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen des Kurses: "
                            + e.getMessage()
            );
            throw new LMSException(
                    "Fehler beim Abrufen des Kurses.",
                    e
            );
        }
    }

    // check if needed to implement this method in CourseManager
   /* public Course findCourseByTitle(String title)  {

        for (Course course : courses.values()) {

            if (course.getTitle().equalsIgnoreCase(title)) {
                return course;
            }
        }

        return null;
    }*/

public List<Course> getAllCourses() throws LMSException {
    List<Course> courses = new ArrayList<>();
    try {
        courses = courseDAO.findAll();
    } catch (SQLException e) {
        LOGGER.warning(
                "Fehler beim Abrufen der Kurse: "
                        + e.getMessage()
        );
        throw new LMSException(
                "Fehler beim Abrufen der Kurse.",
                e
        );
    }
    return courses;
}


public List<Course> searchCourses(String searchPattern)
        throws LMSException {
               try {
        return courseDAO.searchByTitle(searchPattern);

    } catch (SQLException e) {

        LOGGER.warning(
                "Fehler bei der Kurssuche: "
                        + e.getMessage()
        );

        throw new LMSException(
                "Fehler bei der Kurssuche.",
                e
        );
    }
        }

public List<Course> filterCourses(
        CourseFilterStrategy strategy) throws LMSException {

    List<Course> results = new ArrayList<>();
    List<Course> courses =getAllCourses();
    for (Course course : courses) {

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