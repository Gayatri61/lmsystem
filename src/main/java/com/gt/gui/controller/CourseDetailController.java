package com.gt.gui.controller;

import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;
import com.gt.manager.EnrollmentManager;
import com.gt.manager.SessionManager;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Student;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class CourseDetailController {

        private final EnrollmentManager enrollmentManager = EnrollmentManager.getInstance();

        @FXML
        private Button enrollButton;

        @FXML
        private Button unenrollButton;

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
                                course.getTitle());

                descriptionLabel.setText(
                                course.getDescription());

                categoryLabel.setText(
                                course.getCategory().toString());

                difficultyLabel.setText(
                                course.getDifficultyLevel().toString());

                priceLabel.setText(
                                String.format(
                                                "%.2f €",
                                                course.getPrice()));

                instructorLabel.setText(
                                course.getInstructor().getName());

                course.getLessons().forEach(
                                lesson -> lessonListView.getItems().add(
                                                lesson.getTitle()));

                updateEnrollmentButtons();
        }

        @FXML
        private void back() {
                SceneManager.showCourseOverview();
        }

    

@FXML
private void enroll() {

    Student student =
            SessionManager.getCurrentStudent();

    if (student == null) {
        showError(
                "Kein Student eingeloggt."
        );
        return;
    }

    try {

        enrollmentManager.createEnrollment(
                student,
                course
        );

        showInfo(
                "Sie wurden erfolgreich für den Kurs angemeldet."
        );

        updateEnrollmentButtons();

    } catch (LMSException e) {
        showError(e.getMessage());
    }
}

@FXML
private void unenroll() {

    Student student =
            SessionManager.getCurrentStudent();

    if (student == null) {
        showError(
                "Kein Student eingeloggt."
        );
        return;
    }

    try {

        enrollmentManager.unenrollStudent(
                student,
                course
        );

        showInfo(
                "Sie wurden vom Kurs abgemeldet."
        );

        updateEnrollmentButtons();

    } catch (LMSException e) {
        showError(e.getMessage());
    }
}

private void updateEnrollmentButtons() {

    Student student =
            SessionManager.getCurrentStudent();

    if (student == null) {
        enrollButton.setDisable(true);
        unenrollButton.setDisable(true);
        return;
    }

    try {

        Enrollment enrolled =
                enrollmentManager.findEnrollment(
                        student,
                        course
                );
        if (enrolled != null) {
            enrollButton.setDisable(true);
            unenrollButton.setDisable(false);
        } else {
            enrollButton.setDisable(false);
            unenrollButton.setDisable(true);
        }


    } catch (LMSException e) {
        showError(e.getMessage());
    }
}

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private void showInfo(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
