package com.panda.gamelistautoupdater.controllers;


import com.panda.gamelistautoupdater.initializers.FacebookInitializer;
import com.panda.gamelistautoupdater.util.UIUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FBCredentialsController {
    @FXML
    private HBox closeButton;
    @FXML
    private TextField appId;
    @FXML
    private TextField appSecret;
    @FXML
    private TextField appScopeUserId;
    @FXML
    private TextField shortLivePageToken;
    @FXML
    private TextField pageId;


    @FXML
    public void clickOnCloseButton() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void clickOnSetButton() {
        FacebookInitializer.setCredentials(appId.getText(), appSecret.getText(), appScopeUserId.getText(), pageId.getText(), shortLivePageToken.getText());
        clickOnCloseButton();
    }

}
