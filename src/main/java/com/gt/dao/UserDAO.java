package com.gt.dao;



import java.sql.*;

import com.gt.config.DatabaseConnection;
import com.gt.model.Instructor;
import com.gt.model.Student;
import com.gt.model.User;


import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void create(User user) throws SQLException {

        String userSql = """
                INSERT INTO users (name, email, user_type)
                VALUES (?, ?, ?)
                """;

        String studentSql = """
                INSERT INTO students (user_id)
                VALUES (?)
                """;

        String instructorSql = """
                INSERT INTO instructors (user_id, specialization)
                VALUES (?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                int generatedId;

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     userSql,
                                     Statement.RETURN_GENERATED_KEYS)) {

                    statement.setString(1, user.getName());
                    statement.setString(2, user.getEmail());

                    if (user instanceof Student) {
                        statement.setString(3, "STUDENT");
                    } else if (user instanceof Instructor) {
                        statement.setString(3, "INSTRUCTOR");
                    } else {
                        throw new SQLException(
                                "Unbekannter Benutzertyp."
                        );
                    }

                    statement.executeUpdate();

                    try (ResultSet keys =
                                 statement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "Keine ID erzeugt."
                            );
                        }

                        generatedId = keys.getInt(1);
                        user.setId(generatedId);
                    }
                }

                if (user instanceof Student) {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(studentSql)) {

                        statement.setInt(1, generatedId);
                        statement.executeUpdate();
                    }

                } else if (user instanceof Instructor instructor) {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(instructorSql)) {

                        statement.setInt(1, generatedId);
                        statement.setString(
                                2,
                                instructor.getSpecialization()
                        );

                        statement.executeUpdate();
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public User findById(int id) throws SQLException {

        String sql = """
                SELECT
                    u.id,
                    u.name,
                    u.email,
                    u.user_type,
                    i.specialization
                FROM users u
                LEFT JOIN instructors i
                    ON u.id = i.user_id
                WHERE u.id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    public User findByEmail(String email) throws SQLException {

        String sql = """
                SELECT
                    u.id,
                    u.name,
                    u.email,
                    u.user_type,
                    i.specialization
                FROM users u
                LEFT JOIN instructors i
                    ON u.id = i.user_id
                WHERE u.email = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapUser(resultSet);
                }
            }
        }

        return null;
    }

    public List<User> findAll() throws SQLException {

        String sql = """
                SELECT
                    u.id,
                    u.name,
                    u.email,
                    u.user_type,
                    i.specialization
                FROM users u
                LEFT JOIN instructors i
                    ON u.id = i.user_id
                ORDER BY u.id
                """;

        List<User> users = new ArrayList<>();

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        }

        return users;
    }

    public void update(User user) throws SQLException {

        String userSql = """
                UPDATE users
                SET name = ?, email = ?, user_type = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                try (PreparedStatement statement =
                             connection.prepareStatement(userSql)) {

                    statement.setString(1, user.getName());
                    statement.setString(2, user.getEmail());

                    if (user instanceof Student) {
                        statement.setString(3, "STUDENT");
                    } else if (user instanceof Instructor) {
                        statement.setString(3, "INSTRUCTOR");
                    } else {
                        throw new SQLException(
                                "Unbekannter Benutzertyp."
                        );
                    }

                    statement.setInt(4, user.getId());

                    statement.executeUpdate();
                }

                if (user instanceof Instructor instructor) {

                    String instructorSql = """
                            UPDATE instructors
                            SET specialization = ?
                            WHERE user_id = ?
                            """;

                    try (PreparedStatement statement =
                                 connection.prepareStatement(
                                         instructorSql)) {

                        statement.setString(
                                1,
                                instructor.getSpecialization()
                        );

                        statement.setInt(2, user.getId());

                        statement.executeUpdate();
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    public void delete(int id) throws SQLException {

        String sql = """
                DELETE FROM users
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private User mapUser(ResultSet resultSet)
            throws SQLException {

        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String email = resultSet.getString("email");
        String userType = resultSet.getString("user_type");

        if ("STUDENT".equalsIgnoreCase(userType)) {

            return new Student(
                    id,
                    name,
                    email
            );
        }

        if ("INSTRUCTOR".equalsIgnoreCase(userType)) {

            String specialization =
                    resultSet.getString("specialization");

            return new Instructor(
                    id,
                    name,
                    email,
                    specialization
            );
        }

        throw new SQLException(
                "Unbekannter user_type: " + userType
        );
    }

    public Student findStudentById(int studentId) throws SQLException {
        String sql = """
                SELECT *
                FROM users u
                JOIN students s ON u.id = s.user_id
                WHERE u.id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next() && "STUDENT".equalsIgnoreCase(resultSet.getString("user_type"))) {
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        }

        return null;
    }

    public Instructor findInstructorById(int instructorId) throws SQLException {
        String sql = """
                SELECT *
                FROM users u
                JOIN instructors i ON u.id = i.user_id
                WHERE u.id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, instructorId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next() && "INSTRUCTOR".equalsIgnoreCase(resultSet.getString("user_type"))) {
                    return new Instructor(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("specialization")
                    );
                }
            }
        }

        return null;
    }
}