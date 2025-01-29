package com.panda.gamelistautoupdater.controllers;

import com.panda.gamelistautoupdater.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MainController {
    @FXML
    private HBox minimizeButton;
    @FXML
    private HBox closeButton;

    public void initialize() {
        makeMenuButtonWorks();
    }

    @FXML
    public void clickOnUpdateButton() throws IOException {
        displayFbCredentialsView();
    }


    private void makeMenuButtonWorks() {
        minimizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = (Stage) minimizeButton.getScene().getWindow();
            stage.setIconified(true);
        });
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }
    private void displayFbCredentialsView() throws IOException {
        Stage fbStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fb-credentials-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Main.makeStageDraggable(fbStage, scene);
        scene.setFill(Color.TRANSPARENT);
        fbStage.initStyle(StageStyle.UNDECORATED);
        fbStage.initModality(Modality.APPLICATION_MODAL);
        fbStage.setScene(scene);
        fbStage.show();
    }

}