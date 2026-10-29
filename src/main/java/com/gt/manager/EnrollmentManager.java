package com.gt.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gt.dao.EnrollmentDAO;
import com.gt.exception.LMSException;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Lesson;
import com.gt.model.LessonProgress;
import com.gt.model.Student;

public class EnrollmentManager {

    private static final Logger LOGGER = Logger.getLogger(EnrollmentManager.class.getName());

    private static final EnrollmentManager INSTANCE = null;

    private final EnrollmentDAO enrollmentDAO;

    private EnrollmentManager() {
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public static EnrollmentManager getInstance() {
        if (INSTANCE == null) {
            return new EnrollmentManager();
        }

        return INSTANCE;
    }

    // Student einschreiben
    public Enrollment createEnrollment(
            Student student,
            Course course) throws LMSException {
        try {
            Enrollment enrollment = new Enrollment(
                    student,
                    course);

            enrollmentDAO.create(enrollment);

            LOGGER.info(
                    "Student eingeschrieben: Student-ID="
                            + student.getId()
                            + ", Kurs-ID="
                            + course.getId());

            return enrollment;

        } catch (SQLException e) {

            if ("23505".equals(e.getSQLState())) {
                throw new LMSException(
                        "Student ist bereits für diesen Kurs eingeschrieben.",
                        e);
            }

            throw new LMSException(
                    "Fehler beim Einschreiben des Studenten.",
                    e);
        }
    }

    // Student ausschreiben
    public void unenrollStudent(
            Student student,
            Course course) throws LMSException {

        Enrollment enrollment = findEnrollment(student, course);

        if (enrollment == null) {

            LOGGER.warning(
                    "Ausschreibung fehlgeschlagen: "
                            + "keine Einschreibung gefunden.");
            throw new LMSException(
                    "Keine Einschreibung gefunden.");
        }

        try {
            enrollmentDAO.delete(enrollment.getId());
        } catch (SQLException e) {
            throw new LMSException(
                    "Fehler beim Ausschreiben des Studenten.",
                    e);
        }

        LOGGER.info(
                "Student ausgeschrieben: Student-ID="
                        + student.getId()
                        + ", Kurs-ID="
                        + course.getId());
    }

    // Bestimmte Einschreibung suchen
    public Enrollment findEnrollment(
            Student student,
            Course course) throws LMSException {

        try {
            return enrollmentDAO.findByStudentAndCourse(student, course);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Suchen der Einschreibung: "
                            + e.getMessage()
            );

            throw new LMSException(
                    "Fehler beim Suchen der Einschreibung.",
                    e
            );

        }
       
    }

    public boolean isStudentEnrolled(
            Student student,
            Course course) throws LMSException {

        return findEnrollment(student, course) != null;
    }

    // Einschreibungen eines Studenten
    public List<Enrollment> getEnrollmentsByStudent (
            Student student) throws LMSException {

        List<Enrollment> result = new ArrayList<>();
        try {
            result = enrollmentDAO.findByStudent(student);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Einschreibungen: "
                            + e.getMessage()
            );

            throw new LMSException(
                    "Fehler beim Abrufen der Einschreibungen.",
                    e
            );
        }
        return result;
    }

    // Einschreibungen eines Kurses
    public List<Enrollment> getEnrollmentsByCourse(
            Course course) throws LMSException {

        List<Enrollment> result = new ArrayList<>();

        try {
            result = enrollmentDAO.findByCourse(course);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Einschreibungen: "
                            + e.getMessage()
            );

            throw new LMSException(
                    "Fehler beim Abrufen der Einschreibungen.",
                    e
            );
        }

        return result;
    }


    public void rateCourse(
            Student student,
            Course course,
            int stars) throws LMSException {

        if (stars < 1 || stars > 5) {
            throw new LMSException(
                    "Bewertung muss zwischen 1 und 5 Sternen liegen.");
        }

        Enrollment enrollment = findEnrollment(student, course);

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        enrollment.setRating(stars);
    }

    public double getAverageRating(Course course) throws LMSException {

        List<Enrollment> courseEnrollments = getEnrollmentsByCourse(course);

        int totalStars = 0;
        int ratingCount = 0;

        for (Enrollment enrollment : courseEnrollments) {

            if (enrollment.getRating() != null) {

                totalStars += enrollment.getRating();
                ratingCount++;
            }
        }

        if (ratingCount == 0) {
            return 0.0;
        }

        return (double) totalStars / ratingCount;
    }

    public void markLessonAsCompleted(
            Student student,
            Course course,
            Lesson lesson) throws LMSException {

        Enrollment enrollment = findEnrollment(student, course);

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        for (LessonProgress progress : enrollment.getLessonProgressList()) {

            if (progress.getLesson().getId() == lesson.getId()) {

                progress.setCompleted(true);
                return;
            }
        }

        throw new LMSException(
                "Lektion wurde nicht gefunden.");
    }

    public double calculateCourseProgress(
            Student student,
            Course course) throws LMSException {

        Enrollment enrollment = findEnrollment(student, course);

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        List<LessonProgress> progressList = enrollment.getLessonProgressList();

        if (progressList.isEmpty()) {
            return 0.0;
        }

        int completedLessons = 0;

        for (LessonProgress progress : progressList) {

            if (progress.isCompleted()) {
                completedLessons++;
            }
        }

        return (double) completedLessons
                / progressList.size()
                * 100;
    }

}