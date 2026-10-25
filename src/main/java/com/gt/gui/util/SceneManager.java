package com.gt.gui.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    private static void changeScene(String fxmlFile) {

        try {
            FXMLLoader loader =
                    new FXMLLoader(
                            SceneManager.class.getResource(
                                    "/com/gt/gui/"
                                            + fxmlFile
                            )
                    );

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
