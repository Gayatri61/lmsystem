package com.gt.dao;



import java.sql.*;
import java.util.List;

import com.gt.config.DatabaseConnection;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Student;
import com.gt.model.User;

public class EnrollmentDAO {

    private final UserDAO userDAO =
            new UserDAO();

    private final CourseDAO courseDAO =
            new CourseDAO();


    public void create(Enrollment enrollment)
            throws SQLException {

        String sql = """
                INSERT INTO enrollments (
                    student_id,
                    course_id,
                    enrollment_date,
                    rating
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setInt(
                    1,
                    enrollment.getStudent().getId()
            );

            statement.setInt(
                    2,
                    enrollment.getCourse().getId()
            );

            statement.setObject(
                    3,
                    enrollment.getEnrollmentDate()
            );

            if (enrollment.getRating() == null) {

                statement.setNull(
                        4,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        4,
                        enrollment.getRating()
                );
            }

            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {

                if (keys.next()) {
                    enrollment.setId(
                            keys.getInt(1)
                    );
                }
            }
        }
    }

        public Enrollment findById(int id)
            throws SQLException {

        String sql = """
                SELECT *
                FROM enrollments
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

                int studentId =
                        resultSet.getInt(
                                "student_id"
                        );

                int courseId =
                        resultSet.getInt(
                                "course_id"
                        );

                User user =
                        userDAO.findById(studentId);

                if (!(user instanceof Student student)) {
                    throw new SQLException(
                            "Student nicht gefunden."
                    );
                }

                Course course =
                        courseDAO.findById(courseId);

                if (course == null) {
                    throw new SQLException(
                            "Kurs nicht gefunden."
                    );
                }

                Enrollment enrollment =
                        new Enrollment();

                enrollment.setId(
                        resultSet.getInt("id")
                );

                enrollment.setStudent(student);
                enrollment.setCourse(course);

                enrollment.setEnrollmentDate(
                        resultSet
                                .getObject(
                                        "enrollment_date",
                                        java.time.LocalDate.class
                                )
                );

                enrollment.setRating(
                        (Integer) resultSet.getObject(
                                "rating"
                        )
                );

                return enrollment;
            }
        }
    }

        public void update(Enrollment enrollment)
            throws SQLException {

        String sql = """
                UPDATE enrollments
                SET student_id = ?,
                    course_id = ?,
                    enrollment_date = ?,
                    rating = ?
                WHERE id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    enrollment.getStudent().getId()
            );

            statement.setInt(
                    2,
                    enrollment.getCourse().getId()
            );

            statement.setObject(
                    3,
                    enrollment.getEnrollmentDate()
            );

            if (enrollment.getRating() == null) {

                statement.setNull(
                        4,
                        Types.INTEGER
                );

            } else {

                statement.setInt(
                        4,
                        enrollment.getRating()
                );
            }

            statement.setInt(
                    5,
                    enrollment.getId()
            );

            statement.executeUpdate();
        }
    }

        public void delete(int id)
            throws SQLException {

        String sql = """
                DELETE FROM enrollments
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

   public Enrollment findByStudentAndCourse(Student student, Course course)   throws SQLException{

        String sql = """
                SELECT *
                FROM enrollments
                WHERE student_id = ? AND course_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
                   PreparedStatement statement =
                         connection.prepareStatement(sql)) {
                statement.setInt(1, student.getId());
                statement.setInt(2, course.getId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                        Enrollment enrollment = new Enrollment();
                        enrollment.setId(resultSet.getInt("id"));
                        enrollment.setStudent(student);
                        enrollment.setCourse(course);
                        enrollment.setEnrollmentDate(resultSet.getObject("enrollment_date", java.time.LocalDate.class));
                        enrollment.setRating((Integer) resultSet.getObject("rating"));
                        return enrollment;
                        } else {
                        return null;
                }
        }
  
   }

   public List<Enrollment> findByStudent(Student student) throws SQLException {
       
        String sql = """
                SELECT *
                FROM enrollments
                WHERE student_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, student.getId());

            ResultSet resultSet = statement.executeQuery();

            List<Enrollment> enrollments = new java.util.ArrayList<>();

            while (resultSet.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setId(resultSet.getInt("id"));
                enrollment.setStudent(student);

                int courseId = resultSet.getInt("course_id");
                Course course = courseDAO.findById(courseId);
                enrollment.setCourse(course);

                enrollment.setEnrollmentDate(
                        resultSet.getObject(
                                "enrollment_date",
                                java.time.LocalDate.class
                        )
                );

                enrollment.setRating(
                        (Integer) resultSet.getObject("rating")
                );

                enrollments.add(enrollment);
            }

            return enrollments;
        } 
    
   }

   public List<Enrollment> findByCourse(Course course) throws SQLException {
       String sql = """
               SELECT *
               FROM enrollments
               WHERE course_id = ?
               """;

       try (Connection connection =
                    DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

           statement.setInt(1, course.getId());

           ResultSet resultSet = statement.executeQuery();

           List<Enrollment> enrollments = new java.util.ArrayList<>();

           while (resultSet.next()) {
               Enrollment enrollment = new Enrollment();
               enrollment.setId(resultSet.getInt("id"));
               enrollment.setCourse(course);

               int studentId = resultSet.getInt("student_id");
              Student student = userDAO.findStudentById(studentId);
               enrollment.setStudent(student);

               enrollment.setEnrollmentDate(
                       resultSet.getObject(
                               "enrollment_date",
                               java.time.LocalDate.class
                       )
               );

               enrollment.setRating(
                       (Integer) resultSet.getObject("rating")
               );

               enrollments.add(enrollment);
           }

           return enrollments;
       } 
   
  }
}
