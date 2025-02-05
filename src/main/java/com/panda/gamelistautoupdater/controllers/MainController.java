package com.panda.gamelistautoupdater.controllers;

import com.panda.gamelistautoupdater.Main;
import com.panda.gamelistautoupdater.domains.scraping.IggGameWebScraper;
import com.panda.gamelistautoupdater.initializers.ChromeInitializer;
import com.panda.gamelistautoupdater.initializers.FacebookInitializer;
import com.panda.gamelistautoupdater.initializers.YoutubeInitializer;
import com.panda.gamelistautoupdater.util.UIUtility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML
    private HBox minimizeButton;
    @FXML
    private HBox closeButton;
    @FXML
    private TextField ytCredentialPathLabel;
    @FXML
    private Button browseButton;

    @FXML
    private CheckBox ytCheckbox;
    @FXML
    private CheckBox fbCheckbox;
    @FXML
    private TextField browserField;

    public void initialize() {
        ytCredentialPathLabel.setText(System.getenv(YoutubeInitializer.YOUTUBE_CREDENTIAL_ENV));
        makeMenuButtonWorks();
    }

    @FXML
    public void clickOnUpdateButton() {
        if(!startAppInitializationProcess()) return;
    }


    @FXML
    public void clickOnBrowseButton() {
        String credentialPath = showFileChooserDialog();
        if(credentialPath != null) {
            YoutubeInitializer.addYoutubeCredentialEnv(credentialPath);
            ytCredentialPathLabel.setText(credentialPath);
        }
    }

    private boolean startAppInitializationProcess() {
        boolean flag = true;
        try {
            ChromeInitializer.initialize();
            if(fbCheckbox.isSelected()) {
                FacebookInitializer.initialize();
            }
            if(ytCheckbox.isSelected()) {
                if(!YoutubeInitializer.initialize()) {
                    throw new Exception("""
                    - Something wrong with youtube api
                    - Maybe credentials path or file is wrong
                    - Or internet connection issue
                    """);
                }
            }
        } catch (Exception ioe) {
            flag = false;
            UIUtility.showDialog(ioe.getMessage());
        }
        return flag;
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
    private String showFileChooserDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Credentials.json file");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Json Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(ytCredentialPathLabel.getScene().getWindow());
        return selectedFile==null ? null : selectedFile.getAbsolutePath();
    }

}