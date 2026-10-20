package com.gt.manager;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.gt.exception.LMSException;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Student;

public class EnrollmentManager {

    private static final Logger LOGGER =
        Logger.getLogger(EnrollmentManager.class.getName());

    private static final EnrollmentManager INSTANCE =
            new EnrollmentManager();

    private final Map<Integer, Enrollment> enrollments;

    private int nextEnrollmentId;

    private EnrollmentManager() {
        enrollments = new HashMap<>();
        nextEnrollmentId = 1;
    }

    public static EnrollmentManager getInstance() {
        return INSTANCE;
    }

    // Student einschreiben
    public Enrollment enrollStudent(
            Student student,
            Course course) throws LMSException {

        if (student == null) {
            LOGGER.warning(
                "Einschreibung fehlgeschlagen: Student ist null."
            );
            throw new LMSException(
                    "Student darf nicht null sein."
            );
        }

        if (course == null) {

            LOGGER.warning(
                "Einschreibung fehlgeschlagen: Kurs ist null."
            );
            throw new LMSException(
                    "Kurs darf nicht null sein."
            );
        }

        if (isStudentEnrolled(student, course)) {

        LOGGER.warning(
                "Doppelte Einschreibung: Student-ID="
                + student.getId()
                + ", Kurs-ID="
                + course.getId()
        );
            throw new LMSException(
                    "Student ist bereits im Kurs eingeschrieben."
            );
        }

        Enrollment enrollment = new Enrollment(
                nextEnrollmentId++,
                student,
                course
        );

        enrollments.put(
                enrollment.getId(),
                enrollment
        );

            LOGGER.info(
            "Student eingeschrieben: Student-ID="
            + student.getId()
            + ", Kurs-ID="
            + course.getId()
    );


        return enrollment;
    }

    // Student ausschreiben
    public void unenrollStudent(
            Student student,
            Course course) throws LMSException {

        Enrollment enrollment =
                findEnrollment(student, course);

        if (enrollment == null) {

            LOGGER.warning(
                "Ausschreibung fehlgeschlagen: "
                + "keine Einschreibung gefunden."
            );
            throw new LMSException(
                    "Keine Einschreibung gefunden."
            );
        }

        enrollments.remove(enrollment.getId());

        LOGGER.info(
            "Student ausgeschrieben: Student-ID="
            + student.getId()
            + ", Kurs-ID="
            + course.getId()
        );
    }

    // Bestimmte Einschreibung suchen
    public Enrollment findEnrollment(
            Student student,
            Course course) {

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudent().getId()
                    == student.getId()
                    && enrollment.getCourse().getId()
                    == course.getId()) {

                return enrollment;
            }
        }

        return null;
    }

    public boolean isStudentEnrolled(
            Student student,
            Course course) {

        return findEnrollment(student, course) != null;
    }

    // Einschreibungen eines Studenten
    public List<Enrollment> getEnrollmentsByStudent(
            Student student) {

        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getStudent().getId()
                    == student.getId()) {

                result.add(enrollment);
            }
        }

        return result;
    }

    // Einschreibungen eines Kurses
    public List<Enrollment> getEnrollmentsByCourse(
            Course course) {

        List<Enrollment> result = new ArrayList<>();

        for (Enrollment enrollment : enrollments.values()) {

            if (enrollment.getCourse().getId()
                    == course.getId()) {

                result.add(enrollment);
            }
        }

        return result;
    }

    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments.values());
    }
}