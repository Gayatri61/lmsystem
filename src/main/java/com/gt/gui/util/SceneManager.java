package com.gt.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;



import com.gt.gui.controller.CourseDetailController;
import com.gt.model.Course;

import java.net.URL;

public final class SceneManager {

    private static Stage primaryStage;

    private SceneManager() {
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void showStartView() {
        changeScene("start-view.fxml");
    }

    public static void showLoginView() {
        changeScene("login-view.fxml");
    }

    public static void showRegisterView() {
        changeScene("register-view.fxml");
    }

    public static void showCourseOverview() {
        changeScene("course-overview.fxml");
    }

    public static void showCourseDetail(Course course) {

        try {

            URL resource =
                    SceneManager.class.getResource(
                            "/com/gt/gui/course-detail.fxml"
                    );

            if (resource == null) {
                throw new IllegalArgumentException(
                        "course-detail.fxml nicht gefunden."
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(resource);

            Scene scene =
                    new Scene(loader.load());

            CourseDetailController controller =
                    loader.getController();

            controller.setCourse(course);

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Kursdetailansicht konnte nicht geladen werden.",
                    e
            );
        }
    }

    private static void changeScene(String fxmlFile) {

        try {

            URL resource =
                    SceneManager.class.getResource(
                            "/com/gt/gui/" + fxmlFile
                    );

            if (resource == null) {
                throw new IllegalArgumentException(
                        "FXML-Datei nicht gefunden: "
                                + fxmlFile
                );
            }

            FXMLLoader loader =
                    new FXMLLoader(resource);

            Scene scene =
                    new Scene(loader.load());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {

            throw new RuntimeException(
                    "View konnte nicht geladen werden: "
                            + fxmlFile,
                    e
            );
        }
    }
}
