package com.gt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gt.config.DatabaseConnection;
import com.gt.model.Lesson;

public class LessonDAO {
    
    public List<Lesson> findByCourseId(int courseId)
        throws SQLException {

    String sql = """
            SELECT id, title, content
            FROM lessons
            WHERE course_id = ?
            ORDER BY id
            """;

    List<Lesson> lessons = new ArrayList<>();

    try (Connection connection =
                 DatabaseConnection.getConnection();

         PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setInt(1, courseId);

        try (ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                Lesson lesson = new Lesson();

                lesson.setId(
                        resultSet.getInt("id")
                );

                lesson.setTitle(
                        resultSet.getString("title")
                );

                lesson.setContent(
                        resultSet.getString("content")
                );

                lessons.add(lesson);
            }
        }
    }

    return lessons;
}
}
