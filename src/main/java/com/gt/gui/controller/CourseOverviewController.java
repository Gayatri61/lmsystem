package com.gt.gui.controller;

import com.gt.enums.CourseCategory;
import com.gt.enums.DifficultyLevel;
import com.gt.exception.LMSException;
import com.gt.filter.CategoryFilter;
import com.gt.filter.DifficultyFilter;
import com.gt.gui.util.SceneManager;
import com.gt.manager.CourseManager;
import com.gt.model.Course;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class CourseOverviewController {

    @FXML
    private ListView<Course> courseListView;

    private final CourseManager courseManager = CourseManager.getInstance();

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<CourseCategory> categoryComboBox;

    @FXML
    private ComboBox<DifficultyLevel> difficultyComboBox;

    @FXML
    public void initialize() {

        categoryComboBox.getItems().setAll(
                CourseCategory.values());

        difficultyComboBox.getItems().setAll(
                DifficultyLevel.values());

        loadCourses();

    }

    private void loadCourses() {

        try {
            List<Course> courses = courseManager.getAllCourses();

            courseListView.getItems().setAll(courses);

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void searchCourses() {

        String searchText = searchField.getText();

        if (searchText == null ||
                searchText.isBlank()) {

            loadCourses();
            return;
        }

        try {
            List<Course> courses = courseManager.searchCourses(
                    searchText);

            courseListView.getItems()
                    .setAll(courses);

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void showCourseDetails() {

        Course selectedCourse = courseListView
                .getSelectionModel()
                .getSelectedItem();

        if (selectedCourse == null) {
            showError(
                    "Bitte wählen Sie einen Kurs aus.");
            return;
        }

        SceneManager.showCourseDetail(selectedCourse);
    }

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    @FXML
private void filterCourses() {

    CourseCategory category =
            categoryComboBox.getValue();

    DifficultyLevel difficulty =
            difficultyComboBox.getValue();

    try {

        List<Course> courses;

        if (category != null) {

            courses =
                    courseManager.filterCourses(
                            new CategoryFilter(category)
                    );

        } else if (difficulty != null) {

            courses =
                    courseManager.filterCourses(
                            new DifficultyFilter(difficulty)
                    );

        } else {

            courses =
                    courseManager.getAllCourses();
        }

        courseListView.getItems()
                .setAll(courses);

    } catch (LMSException e) {
        showError(e.getMessage());
    }
}

@FXML
private void resetCourses() {

    searchField.clear();

    categoryComboBox
            .getSelectionModel()
            .clearSelection();

    difficultyComboBox
            .getSelectionModel()
            .clearSelection();

    loadCourses();
}
}
