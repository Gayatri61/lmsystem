package com.gt.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gt.backup.BackupData;
import com.gt.backup.CourseBackup;
import com.gt.backup.EnrollmentBackup;
import com.gt.backup.InstructorBackup;
import com.gt.backup.LessonBackup;
import com.gt.backup.LessonProgressBackup;
import com.gt.backup.StudentBackup;
import com.gt.backup.UserBackup;
import com.gt.config.DatabaseConnection;
import com.gt.dao.CourseDAO;
import com.gt.dao.EnrollmentDAO;
import com.gt.dao.LessonDAO;
import com.gt.dao.LessonProgressDAO;
import com.gt.dao.UserDAO;
import com.gt.exception.LMSException;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Instructor;
import com.gt.model.Lesson;
import com.gt.model.LessonProgress;
import com.gt.model.Student;
import com.gt.model.User;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackupManager {

    private static BackupManager instance;

    private static final Logger LOGGER =
            Logger.getLogger(BackupManager.class.getName());

    private final ObjectMapper objectMapper;

    private final UserDAO userDAO = new UserDAO();
    private final CourseDAO courseDAO = new CourseDAO();    
    private final LessonDAO lessonDAO = new LessonDAO();
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    

   private BackupManager() {
    objectMapper = new ObjectMapper();

    objectMapper.registerModule(new JavaTimeModule());

    objectMapper.disable(
        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
    );
}

    public static synchronized BackupManager getInstance() {

        if (instance == null) {
            instance = new BackupManager();
        }

        return instance;
    }

    public synchronized void createBackup(String fileName)
            throws LMSException {

        try {

            // Daten aus der Datenbank holen
            BackupData backupData = loadDataFromDatabase();

            // JSON-Datei erstellen
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), backupData);

            LOGGER.info("Backup erfolgreich erstellt: " + fileName);

        } catch (IOException e) {

            LOGGER.log(
                    Level.SEVERE,
                    "Fehler beim Erstellen des Backups",
                    e
            );

            throw new LMSException(
                    "Backup konnte nicht erstellt werden.",
                    e
            );
        }
    }

    public synchronized void restoreBackup(String fileName)
            throws LMSException {

        try {

            BackupData backupData =
                    objectMapper.readValue(
                            new File(fileName),
                            BackupData.class
                    );

            restoreDataToDatabase(backupData);

            LOGGER.info(
                    "Backup erfolgreich wiederhergestellt: "
                    + fileName
            );

        } catch (IOException e) {

            LOGGER.log(
                    Level.SEVERE,
                    "Fehler beim Wiederherstellen",
                    e
            );

            throw new LMSException(
                    "Backup konnte nicht wiederhergestellt werden.",
                    e
            );
        }
    }

    private BackupData loadDataFromDatabase()
            throws LMSException {

    try {

        List<UserBackup> userBackups = getUserBackups();
      
        List<StudentBackup> studentBackups = getStudentBackups();

        List<InstructorBackup> instructorBackups = getInstructorBackups();

        List<CourseBackup> courseBackups = getAllCoursesBackups();
        
        List<LessonBackup> lessonsBackups = getAllLessonsBackups();

        List<EnrollmentBackup> enrollmentsBackups = getEnrollmentBackups();  
        
        List<LessonProgressBackup> lessonProgressBackups = getLessonProgressBackups();
       
        return new BackupData(userBackups , studentBackups,instructorBackups, courseBackups, lessonsBackups, enrollmentsBackups, lessonProgressBackups );

    } catch (SQLException e) {

        throw new LMSException(
                "Daten konnten nicht gelesen werden.",
                e
        );
    
}

}

    private List<LessonProgressBackup> getLessonProgressBackups() {
       LessonProgressDAO lessonProgressDAO = new LessonProgressDAO();
        try {
            List<LessonProgress> lessonProgressList = lessonProgressDAO.findAll();
            List<LessonProgressBackup> lessonProgressBackups = lessonProgressList.stream()
                    .map(progress -> new LessonProgressBackup(
                            progress.getEnrollment().getId(),
                            progress.getLesson().getId(),
                            progress.isCompleted()
                    ))
                    .toList();
            return lessonProgressBackups;
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen des Lernfortschritts: "
                            + e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    private List<EnrollmentBackup> getEnrollmentBackups() {
        try {
            List<Enrollment> enrollments = enrollmentDAO.findAll();
            List<EnrollmentBackup> enrollmentBackups = enrollments.stream()
                    .map(enrollment -> new EnrollmentBackup(
                            enrollment.getId(),
                            enrollment.getStudent().getId(),
                            enrollment.getCourse().getId(),
                            enrollment.getEnrollmentDate(),
                            enrollment.getRating()
                    ))
                    .toList();
            return enrollmentBackups;
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Einschreibungen: "
                            + e.getMessage()
            );
            return new ArrayList<>();
        }
    }

    private List<LessonBackup> getAllLessonsBackups() {
        try {
            List<Lesson> lessons = lessonDAO.findAll();
            List<LessonBackup> lessonBackups = lessons.stream()
                    .map(lesson -> new LessonBackup(
                            lesson.getId(),
                            lesson.getTitle(),
                            lesson.getContent(),
                            lesson.getCourseId()
                    ))
                    .toList();
            return lessonBackups;
        } catch (SQLException e) {
            LOGGER.warning(
                    "Fehler beim Abrufen der Lektionen: "
                            + e.getMessage()
            );
            return new ArrayList<>();
        }
       
    }

    private List<CourseBackup> getAllCoursesBackups() throws SQLException {
        List<Course> courses = courseDAO.findAll();
        List<CourseBackup> courseBackups = courses.stream()
                .map(course -> new CourseBackup(
                        course.getId(),
                        course.getTitle(),
                        course.getDescription(),
                        course.getInstructor().getId(),
                        course.getCategory(),
                        course.getDifficultyLevel(),
                        course.getPrice()
                ))
                .toList();
        return courseBackups;
    }

    private List<InstructorBackup> getInstructorBackups() throws SQLException {
        List<Instructor> instructors = userDAO.findAllInstructors();
        List<InstructorBackup> instructorBackups = instructors.stream()
                .map(instructor -> new InstructorBackup(
                        instructor.getId(),
                        instructor.getSpecialization()
                ))
                .toList();
        return instructorBackups;
    }

    private List<StudentBackup> getStudentBackups() throws SQLException {
          List<Student> students = userDAO.findAllStudents();
        return students.stream()
                .map(student -> new StudentBackup(student.getId())).toList();
    }

    private List<UserBackup> getUserBackups() throws SQLException {
        List<User> users = userDAO.findAll();
        List<UserBackup> userBackups = users.stream()
                .map(user -> new UserBackup(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getUserType()
                ))
                .toList();
        return userBackups;
    }




   

private void restoreDataToDatabase(BackupData backupData)
        throws LMSException {

    try (Connection connection =
                 DatabaseConnection.getConnection()) {

        connection.setAutoCommit(false);

        try {

            restoreUsers(connection, backupData.getUsers());

            restoreStudents(
                    connection,
                    backupData.getStudents()
            );

            restoreInstructors(
                    connection,
                    backupData.getInstructors()
            );

            restoreCourses(
                    connection,
                    backupData.getCourses()
            );

            restoreLessons(
                    connection,
                    backupData.getLessons()
            );

            restoreEnrollments(
                    connection,
                    backupData.getEnrollments()
            );

            restoreLessonProgress(
                    connection,
                    backupData.getLessonProgress()
            );

            connection.commit();

        } catch (SQLException e) {

            connection.rollback();

            throw new LMSException(
                    "Backup konnte nicht wiederhergestellt werden.",
                    e
            );

        } finally {
            connection.setAutoCommit(true);
        }

    } catch (SQLException e) {

        throw new LMSException(
                "Datenbankfehler beim Wiederherstellen.",
                e
        );
    }
}

private void restoreUsers(
        Connection connection,
        List<UserBackup> users)
        throws SQLException {

    String sql = """
        INSERT INTO users
            (id, name, email, user_type)
        OVERRIDING SYSTEM VALUE
        VALUES (?, ?, ?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (UserBackup user : users) {

            statement.setInt(
                    1,
                    user.getId()
            );

            statement.setString(
                    2,
                    user.getName()
            );

            statement.setString(
                    3,
                    user.getEmail()
            );

            statement.setString(
                    4,
                    user.getUserType().name()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

private void restoreStudents(
        Connection connection,
        List<StudentBackup> students)
        throws SQLException {

    String sql = """
        INSERT INTO students (user_id)
        VALUES (?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (StudentBackup student : students) {

            statement.setInt(
                    1,
                    student.getUserId()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

private void restoreInstructors(
        Connection connection,
        List<InstructorBackup> instructors)
        throws SQLException {

    String sql = """
        INSERT INTO instructors
            (user_id, specialization)
        VALUES (?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (InstructorBackup instructor : instructors) {

            statement.setInt(
                    1,
                    instructor.getUserId()
            );

            statement.setString(
                    2,
                    instructor.getSpecialization()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

private void restoreCourses(
        Connection connection,
        List<CourseBackup> courses)
        throws SQLException {

    String sql = """
        INSERT INTO courses
            (id, title, description, instructor_id,
             category, difficulty_level, price)
        OVERRIDING SYSTEM VALUE
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (CourseBackup course : courses) {

            statement.setInt(
                    1,
                    course.getId()
            );

            statement.setString(
                    2,
                    course.getTitle()
            );

            statement.setString(
                    3,
                    course.getDescription()
            );

            statement.setInt(
                    4,
                    course.getInstructorId()
            );

            // enum -> String
            statement.setString(
                    5,
                    course.getCategory().name()
            );

            // enum -> String
            statement.setString(
                    6,
                    course.getDifficultyLevel().name()
            );

            statement.setDouble(
                    7,
                    course.getPrice()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

private void restoreLessons(
        Connection connection,
        List<LessonBackup> lessons)
        throws SQLException {

    String sql = """
        INSERT INTO lessons
            (id, course_id, title, content)
        OVERRIDING SYSTEM VALUE
        VALUES (?, ?, ?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (LessonBackup lesson : lessons) {

            statement.setInt(
                    1,
                    lesson.getId()
            );

            statement.setInt(
                    2,
                    lesson.getCourseId()
            );

            statement.setString(
                    3,
                    lesson.getTitle()
            );

            statement.setString(
                    4,
                    lesson.getContent()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

private void restoreEnrollments(
        Connection connection,
        List<EnrollmentBackup> enrollments)
        throws SQLException {

    String sql = """
        INSERT INTO enrollments
            (id, student_id, course_id,
             enrollment_date, rating)
        OVERRIDING SYSTEM VALUE
        VALUES (?, ?, ?, ?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (EnrollmentBackup enrollment : enrollments) {

            statement.setInt(
                    1,
                    enrollment.getId()
            );

            statement.setInt(
                    2,
                    enrollment.getStudentId()
            );

            statement.setInt(
                    3,
                    enrollment.getCourseId()
            );

            statement.setObject(
                    4,
                    enrollment.getEnrollmentDate()
            );

            if (enrollment.getRating() == null) {

                statement.setNull(
                        5,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        5,
                        enrollment.getRating()
                );
            }

            statement.addBatch();
        }

        statement.executeBatch();
    }
}
private void restoreLessonProgress(
        Connection connection,
        List<LessonProgressBackup> progressList)
        throws SQLException {

    String sql = """
        INSERT INTO lesson_progress
            (enrollment_id, lesson_id, completed)
        VALUES (?, ?, ?)
        ON CONFLICT DO NOTHING
        """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        for (LessonProgressBackup progress : progressList) {

            statement.setInt(
                    1,
                    progress.getEnrollmentId()
            );

            statement.setInt(
                    2,
                    progress.getLessonId()
            );

            statement.setBoolean(
                    3,
                    progress.isCompleted()
            );

            statement.addBatch();
        }

        statement.executeBatch();
    }
}

}


