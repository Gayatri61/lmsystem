package com.gt.dao;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.gt.config.DatabaseConnection;
import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;
import com.gt.model.Course;
import com.gt.model.Instructor;
import com.gt.model.Lesson;
import com.gt.model.User;

public class CourseDAO {

    private final UserDAO userDAO =
            new UserDAO();

    private final LessonDAO lessonDAO = new LessonDAO();

    public void create(Course course)
            throws SQLException {

        String sql = """
                INSERT INTO courses (
                    title,
                    description,
                    instructor_id,
                    category,
                    difficulty_level,
                    price
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(
                    1,
                    course.getTitle()
            );

            statement.setString(
                    2,
                    course.getDescription()
            );

            statement.setInt(
                    3,
                    course.getInstructor().getId()
            );

            statement.setString(
                    4,
                    course.getCategory().name()
            );

            statement.setString(
                    5,
                    course.getDifficultyLevel().name()
            );

            statement.setDouble(
                    6,
                    course.getPrice()
            );

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    course.setId(
                            keys.getInt(1)
                    );
                }
            }
        }
    }

        public Course findById(int id)
            throws SQLException {

        String sql = """
                SELECT *
                FROM courses
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {
                    return null;
                }

                int instructorId =
                        resultSet.getInt(
                                "instructor_id"
                        );

                User user =
                        userDAO.findById(
                                instructorId
                        );

                if (!(user instanceof Instructor instructor)) {
                    throw new SQLException(
                            "Instructor nicht gefunden."
                    );
                }

                Course course =
                        new Course();

                course.setId(
                        resultSet.getInt("id")
                );

                course.setTitle(
                        resultSet.getString("title")
                );

                course.setDescription(
                        resultSet.getString(
                                "description"
                        )
                );

                course.setInstructor(instructor);

                course.setCategory(
                        CourseCategory.valueOf(
                                resultSet.getString(
                                        "category"
                                )
                        )
                );

                course.setDifficultyLevel(
                        DifficultyLevel.valueOf(
                                resultSet.getString(
                                        "difficulty_level"
                                )
                        )
                );

                course.setPrice(
                        resultSet.getDouble("price")
                );

                return course;
            }
        }
    }

        public void update(Course course)
            throws SQLException {

        String sql = """
                UPDATE courses
                SET title = ?,
                    description = ?,
                    instructor_id = ?,
                    category = ?,
                    difficulty_level = ?,
                    price = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    course.getTitle()
            );

            statement.setString(
                    2,
                    course.getDescription()
            );

            statement.setInt(
                    3,
                    course.getInstructor().getId()
            );

            statement.setString(
                    4,
                    course.getCategory().name()
            );

            statement.setString(
                    5,
                    course.getDifficultyLevel().name()
            );

            statement.setDouble(
                    6,
                    course.getPrice()
            );

            statement.setInt(
                    7,
                    course.getId()
            );

            statement.executeUpdate();
        }
    }
    
        public void delete(int id)
            throws SQLException {

        String sql = """
                DELETE FROM courses
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

    public List<Course> findAll() 
            throws SQLException {
        String sql = """
                SELECT *
                FROM courses
                """;        
        try (Connection connection =
                     DatabaseConnection.getConnection();
                PreparedStatement statement =
                     connection.prepareStatement(sql);
                ResultSet resultSet =
                     statement.executeQuery()) {
            List<Course> courses = new java.util.ArrayList<>();
            while (resultSet.next()) {
                int instructorId =
                        resultSet.getInt(
                                "instructor_id"
                        );

                User user =
                        userDAO.findById(
                                instructorId
                        );

                if (!(user instanceof Instructor instructor)) {
                    throw new SQLException(
                            "Instructor nicht gefunden."
                    );
                }

                Course course =
                        new Course();

                course.setId(
                        resultSet.getInt("id")
                );

                course.setTitle(
                        resultSet.getString("title")
                );

                course.setDescription(
                        resultSet.getString(
                                "description"
                        )
                );

                course.setInstructor(instructor);

                course.setCategory(
                        CourseCategory.valueOf(
                                resultSet.getString(
                                        "category"
                                )
                        )
                );

                course.setDifficultyLevel(
                        DifficultyLevel.valueOf(
                                resultSet.getString(
                                        "difficulty_level"
                                )
                        )
                );

                course.setPrice(
                        resultSet.getDouble("price")
                );

               
            List<Lesson> lessons = lessonDAO.findByCourseId(course.getId());

            course.setLessons(lessons);

                courses.add(course);
            }
            return courses;
       }
    }

    public List<Course> searchByTitle(String regex)
        throws SQLException {

    String sql = """
            SELECT
                id,
                title,
                description,
                instructor_id,
                category,
                difficulty_level,
                price
            FROM courses
            WHERE title ~* ?
            ORDER BY title
            """;

    List<Course> courses = new ArrayList<>();

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, regex);

        try (ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Course course =
                        mapCourse(resultSet);

                courses.add(course);
            }
        }
    }

    return courses;
}

    private Course mapCourse(ResultSet resultSet) {
        try {
            int instructorId =
                    resultSet.getInt(
                            "instructor_id"
                    );

            User user =
                    userDAO.findById(
                            instructorId
                    );

            if (!(user instanceof Instructor instructor)) {
                throw new SQLException(
                        "Instructor nicht gefunden."
                );
            }

            Course course =
                    new Course();

            course.setId(
                    resultSet.getInt("id")
            );

            course.setTitle(
                    resultSet.getString("title")
            );

            course.setDescription(
                    resultSet.getString(
                            "description"
                    )
            );

            course.setInstructor(instructor);

            course.setCategory(
                    CourseCategory.valueOf(
                            resultSet.getString(
                                    "category"
                            )
                    )
            );

            course.setDifficultyLevel(
                    DifficultyLevel.valueOf(
                            resultSet.getString(
                                    "difficulty_level"
                            )
                    )
            );

            course.setPrice(
                    resultSet.getDouble("price")
            );

            return course;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        

}
