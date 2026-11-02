package com.gt.gui.controller;

import com.gt.manager.BackupManager;
import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class BackupController {

    private final BackupManager backupManager =
            BackupManager.getInstance();

    @FXML
    private void createBackup() {

        Thread thread = new Thread(() -> {

            try {

                backupManager.createBackup("backup.json");

                Platform.runLater(() -> {
                    showMessage(
                            "Backup erfolgreich erstellt."
                    );
                });

            } catch (LMSException e) {

                Platform.runLater(() -> {
                    showError(e.getMessage());
                });
            }
        });

        thread.start();
    }

    @FXML
    private void restoreBackup() {

        Thread thread = new Thread(() -> {

            try {

                backupManager.restoreBackup("backup.json");

                Platform.runLater(() -> {
                    showMessage(
                            "Backup erfolgreich wiederhergestellt."
                    );
                });

            } catch (LMSException e) {

                Platform.runLater(() -> {
                    showError(e.getMessage());
                });
            }
        });

        thread.start();
    }

    private void showMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Backup");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

        @FXML
        private void back() {
                SceneManager.showCourseOverview();
        }
}
