package com.gt.gui.controller;


import com.gt.gui.util.SceneManager;
import com.gt.model.Course;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CourseDetailController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label difficultyLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label instructorLabel;

    @FXML
    private ListView<String> lessonListView;

    private Course course;

    public void setCourse(Course course) {

        this.course = course;

        titleLabel.setText(
                course.getTitle()
        );

        descriptionLabel.setText(
                course.getDescription()
        );

        categoryLabel.setText(
                course.getCategory().toString()
        );

        difficultyLabel.setText(
                course.getDifficultyLevel().toString()
        );

        priceLabel.setText(
                String.format(
                        "%.2f €",
                        course.getPrice()
                )
        );

        instructorLabel.setText(
                course.getInstructor().getName()
        );

        course.getLessons().forEach(
                lesson ->
                        lessonListView.getItems().add(
                                lesson.getTitle()
                        )
        );
    }

    @FXML
    private void back() {
        SceneManager.showCourseOverview();
    }
}
