package com.gt.gui.controller;

import com.gt.exception.LMSException;
import com.gt.gui.util.SceneManager;
import com.gt.manager.UserManager;
import com.gt.model.Student;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    private final UserManager userManager =
            UserManager.getInstance();

    @FXML
    private void register() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {

            showError(
                    "Name und E-Mail dürfen nicht leer sein."
            );

            return;
        }

        try {

            Student student =
                    new Student(
                            0,
                            name,
                            email
                    );

            userManager.addUser(student);

            showSuccess(
                    "Registrierung erfolgreich."
            );

            SceneManager.showLoginView();

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

    private void showSuccess(String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Erfolg");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
