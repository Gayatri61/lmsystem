package com.gt.gui;

import com.gt.config.DatabaseInitializer;
import com.gt.gui.util.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class LMSApplication extends Application {

    @Override
    public void start(Stage stage) {

        SceneManager.setPrimaryStage(stage);

        stage.setTitle(
                "JavaTech Solutions LMS"
        );

        stage.setWidth(900);
        stage.setHeight(600);

        SceneManager.showStartView();
    }

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        launch(args);
    }
}
