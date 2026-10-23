package com.gt.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initialize() {

        String usersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    user_type VARCHAR(20) NOT NULL
                )
                """;

        String studentsTable = """
                CREATE TABLE IF NOT EXISTS students (
                    user_id INTEGER PRIMARY KEY,

                    FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
                )
                """;

        String instructorsTable = """
                CREATE TABLE IF NOT EXISTS instructors (
                    user_id INTEGER PRIMARY KEY,
                    specialization VARCHAR(100),

                    FOREIGN KEY (user_id)
                        REFERENCES users(id)
                        ON DELETE CASCADE
                )
                """;

        String coursesTable = """
                CREATE TABLE IF NOT EXISTS courses (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    title VARCHAR(150) NOT NULL,
                    description TEXT,
                    instructor_id INTEGER NOT NULL,
                    category VARCHAR(50) NOT NULL,
                    difficulty_level VARCHAR(30) NOT NULL,
                    price DECIMAL(10, 2) NOT NULL,

                    FOREIGN KEY (instructor_id)
                        REFERENCES instructors(user_id)
                )
                """;

        String lessonsTable = """
                CREATE TABLE IF NOT EXISTS lessons (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    course_id INTEGER NOT NULL,
                    title VARCHAR(150) NOT NULL,
                    content TEXT,

                    FOREIGN KEY (course_id)
                        REFERENCES courses(id)
                        ON DELETE CASCADE
                )
                """;

        String enrollmentsTable = """
                CREATE TABLE IF NOT EXISTS enrollments (
                    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    student_id INTEGER NOT NULL,
                    course_id INTEGER NOT NULL,
                    enrollment_date DATE NOT NULL,
                    rating INTEGER,

                    FOREIGN KEY (student_id)
                        REFERENCES students(user_id),

                    FOREIGN KEY (course_id)
                        REFERENCES courses(id)
                        ON DELETE CASCADE,

                    UNIQUE (student_id, course_id),

                    CHECK (
                        rating IS NULL
                        OR rating BETWEEN 1 AND 5
                    )
                )
                """;

        String lessonProgressTable = """
                CREATE TABLE IF NOT EXISTS lesson_progress (
                    enrollment_id INTEGER NOT NULL,
                    lesson_id INTEGER NOT NULL,
                    completed BOOLEAN NOT NULL DEFAULT FALSE,

                    PRIMARY KEY (
                        enrollment_id,
                        lesson_id
                    ),

                    FOREIGN KEY (enrollment_id)
                        REFERENCES enrollments(id)
                        ON DELETE CASCADE,

                    FOREIGN KEY (lesson_id)
                        REFERENCES lessons(id)
                        ON DELETE CASCADE
                )
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             Statement statement =
                     connection.createStatement()) {

            statement.execute(usersTable);
            statement.execute(studentsTable);
            statement.execute(instructorsTable);
            statement.execute(coursesTable);
            statement.execute(lessonsTable);
            statement.execute(enrollmentsTable);
            statement.execute(lessonProgressTable);

            System.out.println(
                    "Datenbank erfolgreich initialisiert."
            );

        } catch (SQLException e) {

            System.err.println(
                    "Fehler bei der Datenbankinitialisierung: "
                            + e.getMessage()
            );
        }
    }
}