package com.gt.gui.controller;

import java.util.List;

import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;
import com.gt.manager.EnrollmentManager;
import com.gt.manager.SessionManager;
import com.gt.model.Course;
import com.gt.model.Enrollment;
import com.gt.model.Lesson;
import com.gt.model.LessonProgress;
import com.gt.model.Student;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    private ListView<String> lessonListView = new ListView<>();

    private Course course;

    @FXML
    private Label averageRatingLabel;

    @FXML
    private ComboBox<Integer> ratingComboBox;

    @FXML
    private Label ratingMessageLabel;

    @FXML
    private Button rateButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label progressLabel;

    @FXML
    private Label lessonCountLabel;

    @FXML
    private VBox lessonContainer;

    @FXML
    public void initialize() {

        ratingComboBox.getItems().addAll(
                1, 2, 3, 4, 5);
    }

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

        loadRating();

        loadStudentRating();

        loadLessonProgress();
    }

    private void loadLessons() {
        lessonContainer.getChildren().clear();

        List<Lesson> lessons = course.getLessons();

        for (Lesson lesson : lessons) {
                    CheckBox checkBox = new CheckBox();

        // Disable only the checkbox
        checkBox.setDisable(true);

        Label lessonTitle = new Label(lesson.getTitle());

        HBox lessonRow = new HBox(10);
        lessonRow.getChildren().addAll(checkBox, lessonTitle);

        lessonContainer.getChildren().add(lessonRow);
        }
    }

    private void loadLessonProgress() {
        Student student = getCurrentStudent();

        try {
            if (student == null ||
                    !enrollmentManager.isStudentEnrolled(student.getId(), course.getId())) {

                loadLessons();
                progressBar.setProgress(0);
                progressLabel.setText("0% abgeschlossen");
                lessonCountLabel.setText("0 / 0 lektionen");

                return;
            }

            // Student is enrolled: load actual progress.
            List<LessonProgress> progressList = enrollmentManager.getLessonProgress(
                    student.getId(), course.getId());

            displayLessons(progressList);
            updateCourseProgress(progressList);
        } catch (LMSException e) {
            showError(e.getMessage());
            

        }

    }

    private void displayLessons(
            List<LessonProgress> progressList) {

        lessonContainer.getChildren().clear();

        for (LessonProgress progress : progressList) {

            CheckBox checkBox = new CheckBox(
                    progress.getLesson().getTitle());

            checkBox.setSelected(progress.isCompleted());

            checkBox.setOnAction(event -> {

                updateLessonCompletion(
                        progress,
                        checkBox.isSelected());

            });

            lessonContainer.getChildren().add(checkBox);
        }
    }

    private void updateLessonCompletion(
            LessonProgress progress,
            boolean completed) {

        Student student = getCurrentStudent();

        if (student == null) {
            return;
        }

        try {

            enrollmentManager.updateLessonProgress(
                    student.getId(),
                    progress.getLesson().getId(),
                    course.getId(),
                    completed);

            progress.setCompleted(completed);

            loadLessonProgress();

        } catch (LMSException e) {

            showError(e.getMessage());

            // Restore the previous checkbox state
            loadLessonProgress();
        }
    }

    private void updateCourseProgress(
            List<LessonProgress> progressList) {

        int totalLessons = progressList.size();

        long completedLessons = progressList.stream()
                .filter(LessonProgress::isCompleted)
                .count();

        if (totalLessons == 0) {

            progressBar.setProgress(0);
            progressLabel.setText("0% completed");
            lessonCountLabel.setText("0 / 0 lessons");

            return;
        }

        double progress = (double) completedLessons / totalLessons;

        progressBar.setProgress(progress);

        int percentage = (int) Math.round(progress * 100);

        progressLabel.setText(
                percentage + "% completed");

        lessonCountLabel.setText(
                completedLessons + " / " + totalLessons
                        + " lessons");
    }

    private Student getCurrentStudent() {

        // String sessionId = CurrentSession.getSessionId();

        // User user = sessionManager.getCurrentUser(sessionId);

        Student student = SessionManager.getCurrentStudent();

        if (student == null) {
            showError(
                    "Kein Student eingeloggt.");
        }
        return student;
    }

    @FXML
    private void rateCourse() {

        Integer rating = ratingComboBox.getValue();

        if (rating == null) {
            showError(
                    "Bitte wähle eine Bewertung von 1 bis 5.");
            return;
        }

        Student student = getCurrentStudent();

        if (student == null) {
            showError(
                    "Kein angemeldeter Student gefunden.");
            return;
        }

        try {

            enrollmentManager.rateCourse(
                    student.getId(),
                    course.getId(),
                    rating);

            ratingMessageLabel.setText(
                    "Bewertung gespeichert.");

            loadRating();

            updateRatingControls();

        } catch (LMSException e) {

            showError(e.getMessage());
        }
    }

    private void loadRating() {

        try {

            Double average = enrollmentManager
                    .getAverageRating(course.getId());

            if (average == null) {

                averageRatingLabel.setText(
                        "Noch keine Bewertungen");

            } else {

                averageRatingLabel.setText(
                        createStars(average)
                                + " "
                                + String.format("%.1f / 5", average));
            }
        } catch (LMSException e) {

            averageRatingLabel.setText(
                    "Bewertung konnte nicht geladen werden.");
        }
    }

    private String createStars(double rating) {

        int rounded = (int) Math.round(rating);

        return "★".repeat(rounded)
                + "☆".repeat(5 - rounded);
    }

    private void loadStudentRating() {

        Student student = getCurrentStudent();

        if (student == null) {
            return;
        }

        try {

            Integer rating = enrollmentManager.getRating(
                    student.getId(),
                    course.getId());

            ratingComboBox.setValue(rating);

        } catch (LMSException e) {
            showError(e.getMessage());
        }

    }

    private void updateRatingControls() {

        Student student = getCurrentStudent();

        if (student == null) {
            ratingComboBox.setDisable(true);
            rateButton.setDisable(true);
            return;
        }

        try {

            boolean enrolled = enrollmentManager.findEnrollment(
                    student.getId(),
                    course.getId()) != null;

            ratingComboBox.setDisable(!enrolled);
            rateButton.setDisable(!enrolled);

            if (!enrolled) {
                ratingMessageLabel.setText(
                        "Du musst für diesen Kurs angemeldet sein, "
                                + "um ihn zu bewerten.");
            }

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void back() {
        SceneManager.showCourseOverview();
    }

    @FXML
    private void enroll() {

        Student student = SessionManager.getCurrentStudent();

        if (student == null) {
            showError(
                    "Kein Student eingeloggt.");
            return;
        }

        try {

            enrollmentManager.createEnrollment(
                    student,
                    course);

            showInfo(
                    "Sie wurden erfolgreich für den Kurs angemeldet.");

            updateEnrollmentButtons();

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void unenroll() {

        Student student = SessionManager.getCurrentStudent();

        if (student == null) {
            showError(
                    "Kein Student eingeloggt.");
            return;
        }

        try {

            enrollmentManager.unenrollStudent(
                    student,
                    course);

            showInfo(
                    "Sie wurden vom Kurs abgemeldet.");

            updateEnrollmentButtons();

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    private void updateEnrollmentButtons() {

        Student student = SessionManager.getCurrentStudent();

        if (student == null) {
            enrollButton.setDisable(true);
            unenrollButton.setDisable(true);
            return;
        }

        try {

            Enrollment enrolled = enrollmentManager.findEnrollment(
                    student.getId(),
                    course.getId());
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
