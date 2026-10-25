package com.gt.gui.controller;




import com.gt.gui.util.SceneManager;

import javafx.fxml.FXML;

public class StartController {

    @FXML
    private void openLogin() {
        SceneManager.showLoginView();
    }

    @FXML
    private void openRegister() {
        SceneManager.showRegisterView();
    }
}
