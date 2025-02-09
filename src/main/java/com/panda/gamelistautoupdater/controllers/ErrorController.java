package com.panda.gamelistautoupdater.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ErrorController {
    @FXML
    private TextArea textArea;
    @FXML
    private HBox okButton;

    public void initialize() {
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            ((Stage)okButton.getScene().getWindow()).close();
        });
    }

    public void setErrorMessage(String message) {
        Platform.runLater(()->{textArea.setText(message);});
    }
}