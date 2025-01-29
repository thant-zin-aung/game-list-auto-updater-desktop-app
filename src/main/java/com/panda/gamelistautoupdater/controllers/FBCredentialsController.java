package com.panda.gamelistautoupdater.controllers;


import com.panda.gamelistautoupdater.util.UIUtility;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FBCredentialsController {
    @FXML
    private HBox closeButton;

    @FXML
    public void clickOnCloseButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
