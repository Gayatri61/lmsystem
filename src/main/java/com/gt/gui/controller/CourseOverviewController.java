package com.gt.gui.controller;

import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;    
import com.gt.manager.CourseManager;
import com.gt.model.Course;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;

import java.util.List;

public class CourseOverviewController {

    @FXML
    private ListView<Course> courseListView;

    private final CourseManager courseManager =
            CourseManager.getInstance();

    @FXML
    public void initialize() {

        try {
            List<Course> courses =
                    courseManager.getAllCourses();

                courseListView.getItems().setAll(courses);

        } catch (LMSException e) {

            showError(e.getMessage());
        }
    }

    @FXML
    private void showCourseDetails() {

        Course selectedCourse =
                courseListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (selectedCourse == null) {
            showError(
                    "Bitte wählen Sie einen Kurs aus."
            );
            return;
        }

        SceneManager.showCourseDetail(selectedCourse);
    }

    private void showError(String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
