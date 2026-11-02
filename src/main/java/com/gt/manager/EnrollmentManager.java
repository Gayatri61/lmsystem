package com.gt.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gt.dao.EnrollmentDAO;
import com.gt.dao.LessonDAO;
import com.gt.dao.LessonProgressDAO;
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

    private final LessonProgressDAO lessonprogressDAO;

    private final LessonDAO lessonDAO = new LessonDAO();

    private EnrollmentManager() {
        this.enrollmentDAO = new EnrollmentDAO();
        this.lessonprogressDAO = new LessonProgressDAO();
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

            createLessonProgressForEnrollment(enrollment);

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

    private void createLessonProgressForEnrollment(Enrollment enrollment) throws SQLException {
        List<Lesson> lessons = lessonDAO.findByCourseId(enrollment.getCourse().getId());

        for (Lesson lesson : lessons) {
            LessonProgress lessonProgress = new LessonProgress(
                    enrollment,
                    lesson,
                    false);

            lessonprogressDAO.create(lessonProgress);
        }
    }

    // Student ausschreiben
    public void unenrollStudent(
            Student student,
            Course course) throws LMSException {

        Enrollment enrollment = findEnrollment(student.getId(), course.getId());

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
            int studentId,
            int courseId) throws LMSException {

        try {
            return enrollmentDAO.findByStudentAndCourse(studentId, courseId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Suchen der Einschreibung: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Suchen der Einschreibung.",
                    e);

        }

    }

    public boolean isStudentEnrolled(
            int studentId,
            int courseId) throws LMSException {

        return findEnrollment(studentId, courseId) != null;
    }

    // Einschreibungen eines Studenten
    public List<Enrollment> getEnrollmentsByStudent(
            Student student) throws LMSException {

        List<Enrollment> result = new ArrayList<>();
        try {
            result = enrollmentDAO.findByStudent(student);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Einschreibungen: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Abrufen der Einschreibungen.",
                    e);
        }
        return result;
    }

    // Einschreibungen eines Kurses
    public List<Enrollment> getEnrollmentsByCourse(
            int courseId) throws LMSException {

        List<Enrollment> result = new ArrayList<>();

        try {
            result = enrollmentDAO.findByCourse(courseId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Einschreibungen: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Abrufen der Einschreibungen.",
                    e);
        }

        return result;
    }

    public void rateCourse(
            int studentId,
            int courseId,
            int stars) throws LMSException {

        if (stars < 1 || stars > 5) {
            throw new LMSException(
                    "Bewertung muss zwischen 1 und 5 Sternen liegen.");
        }

        Enrollment enrollment = findEnrollment(studentId, courseId);

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        enrollment.setRating(stars);

        try {
            enrollmentDAO.update(enrollment);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Aktualisieren der Bewertung: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Aktualisieren der Bewertung.",
                    e);
        }
    }

    public Double getAverageRating(int courseId) throws LMSException {

        List<Enrollment> courseEnrollments = getEnrollmentsByCourse(courseId);

        int totalStars = 0;
        int ratingCount = 0;

        for (Enrollment enrollment : courseEnrollments) {

            if (enrollment.getRating() != null) {

                totalStars += enrollment.getRating();
                ratingCount++;
            }
        }

        if (ratingCount == 0) {
            return null;
        }

        return (double) (totalStars / ratingCount);
    }

    public Integer getRating(int studentId, int courseId) throws LMSException {
        Enrollment enrollment = null;
        try {
            enrollment = enrollmentDAO.findByStudentAndCourse(studentId, courseId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Bewertung: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Abrufen der Bewertung.",
                    e);

        }

        if (enrollment != null) {
            return enrollment.getRating();
        } else {
            return null;
        }
    }

    public void markLessonAsCompleted(
            Student student,
            Course course,
            Lesson lesson) throws LMSException {

        Enrollment enrollment = findEnrollment(student.getId(), course.getId());

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        LessonProgress lessonprogress = new LessonProgress(enrollment, lesson, true);
        try {
            lessonprogressDAO.updateLessonProgress(lessonprogress);
        } catch (SQLException e) {

            LOGGER.warning(
                    "Fehler beim markLessonAsCompleted "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim markLessonAsCompleted",
                    e);

        }

    }

    public double calculateCourseProgress(
            Student student,
            Course course) throws LMSException {

        Enrollment enrollment = findEnrollment(student.getId(), course.getId());

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

    public List<LessonProgress> getLessonProgress(int studentId, int courseId) throws LMSException {
        try {

            Enrollment enrollment = findEnrollment(studentId, courseId);
            return lessonprogressDAO.findByEnrollmentId(enrollment.getId());
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der lessonprogress: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Abrufen der lessonprogress.",
                    e);
        }
    }

    public void updateLessonProgress(int studentId, int lessonId, int courseId, boolean completed) throws LMSException {
        Enrollment enrollment = findEnrollment(studentId, courseId);

        if (enrollment == null) {
            throw new LMSException(
                    "Student ist nicht in diesem Kurs eingeschrieben.");
        }

        Lesson lesson = null;
        try {
            lesson = lessonDAO.findById(lessonId);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der lesson: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Abrufen der lesson.",
                    e);
        }

        if (lesson == null) {
            throw new LMSException(
                    "Lesson nicht gefunden.");
        }

        LessonProgress lessonprogress = new LessonProgress(enrollment, lesson, completed);

        try {
            lessonprogressDAO.updateLessonProgress(lessonprogress);
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Aktualisieren des lessonprogress: "
                            + e.getMessage());

            throw new LMSException(
                    "Fehler beim Aktualisieren des lessonprogress.",
                    e);
        }
    }

}