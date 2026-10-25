package com.gt.gui.controller;


import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;
import com.gt.manager.UserManager;
import com.gt.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField emailField;

    private final UserManager userManager =
            UserManager.getInstance();

    @FXML
    private void login() {

        String email =
                emailField.getText().trim();

        if (email.isEmpty()) {
            showError(
                    "Bitte geben Sie eine E-Mail-Adresse ein."
            );
            return;
        }

        try {

            User user =
                    userManager.findUserByEmail(email);

            if (user == null) {
                showError(
                        "Benutzer wurde nicht gefunden."
                );
                return;
            }

            SceneManager.showCourseOverview();

        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void back() {
        SceneManager.showStartView();
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
