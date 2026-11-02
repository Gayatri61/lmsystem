package com.gt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.gt.config.DatabaseConnection;

import com.gt.model.Enrollment;

import com.gt.model.Lesson;
import com.gt.model.LessonProgress;


public class LessonProgressDAO {

        private final EnrollmentDAO enrollmentDAO= new EnrollmentDAO();

        private final LessonDAO lessonDAO=new LessonDAO();

    public List<LessonProgress> findAll() throws SQLException {
       
        String sql = """
                SELECT *
                FROM lesson_progress
                """;        
        try (Connection connection =
                     DatabaseConnection.getConnection();
                PreparedStatement statement =
                     connection.prepareStatement(sql);
                ResultSet resultSet =
                     statement.executeQuery()) {
            List<LessonProgress> lessonProgressList = new java.util.ArrayList<>();
            while (resultSet.next()) {
                LessonProgress lessonProgress = new LessonProgress();
                
               
                int enrollmentId = resultSet.getInt("enrollment_id");

                Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
                lessonProgress.setEnrollment(enrollment);

                int lessonId = resultSet.getInt("lesson_id");
                Lesson lesson=lessonDAO.findById(lessonId);
                lessonProgress.setLesson(lesson);
                lessonProgress.setCompleted(resultSet.getBoolean("completed"));
                lessonProgressList.add(lessonProgress);
            }
            return lessonProgressList;
        }
    }

    public void create(LessonProgress lessonprogress) throws SQLException {
        String sql = "INSERT INTO lesson_progress (enrollment_id, lesson_id, completed) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lessonprogress.getEnrollment().getId());
            statement.setInt(2, lessonprogress.getLesson().getId());
            statement.setBoolean(3, lessonprogress.isCompleted());
            statement.executeUpdate();
        }
    }

    public List<LessonProgress> findByEnrollmentId(int enrollmentId) throws SQLException {
        String sql = "SELECT * FROM lesson_progress WHERE enrollment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, enrollmentId);
            ResultSet resultSet = statement.executeQuery();
            List<LessonProgress> lessonProgressList = new java.util.ArrayList<>();
            while (resultSet.next()) {
                LessonProgress lessonProgress = new LessonProgress();
                Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
                lessonProgress.setEnrollment(enrollment);

                int lessonId = resultSet.getInt("lesson_id");
                Lesson lesson=lessonDAO.findById(lessonId);
                lessonProgress.setLesson(lesson);
                lessonProgress.setCompleted(resultSet.getBoolean("completed"));
                lessonProgressList.add(lessonProgress);
            }
            return lessonProgressList;
        }
    }

	public void updateLessonProgress(LessonProgress lessonprogress) throws SQLException {
        String sql = "UPDATE lesson_progress SET completed = ? WHERE enrollment_id = ? AND lesson_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, lessonprogress.isCompleted());
            statement.setInt(2, lessonprogress.getEnrollment().getId());
            statement.setInt(3, lessonprogress.getLesson().getId());
            statement.executeUpdate();
        }
    }


	}
 

             