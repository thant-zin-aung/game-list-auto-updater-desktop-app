package com.panda.gamelistautoupdater.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ErrorController {
    @FXML
    private TextArea textArea;

    public void setErrorMessage(String message) {
        System.out.println("Error label changed...");
        textArea.clear();
        textArea.setText(message);
    }
}
