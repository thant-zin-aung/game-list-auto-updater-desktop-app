package com.panda.gamelistautoupdater.controllers;

import com.panda.gamelistautoupdater.Main;
import com.panda.gamelistautoupdater.domains.facebook.FacebookHandler;
import com.panda.gamelistautoupdater.domains.scraping.IggGameWebScraper;
import com.panda.gamelistautoupdater.initializers.ChromeInitializer;
import com.panda.gamelistautoupdater.initializers.FacebookInitializer;
import com.panda.gamelistautoupdater.initializers.YoutubeInitializer;
import com.panda.gamelistautoupdater.util.UIUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML
    private HBox minimizeButton;
    @FXML
    private HBox closeButton;
    @FXML
    private TextField ytCredentialPathLabel;
    @FXML
    public CheckBox ytCheckbox;
    @FXML
    public CheckBox fbCheckbox;
    @FXML
    private TextField browserField;
    @FXML
    private TextField pageIndexField;
    @FXML
    private Text pageIndexText;
    @FXML
    private Label gameTitleLabel;
    @FXML
    private Text statusText;
    @FXML
    private HBox extractDetailsWrapper, extractLinksWrapper, extractImagesWrapper, extractYoutubeWrapper, uploadGameWrapper, postFacebookWrapper;

    public void initialize() {
        ytCredentialPathLabel.setText(System.getenv(YoutubeInitializer.YOUTUBE_CREDENTIAL_ENV));
        makeMenuButtonWorks();
        initializeUI();
        ChromeInitializer.initialize();
    }

    public void initializeUI() {
        Platform.runLater(()->{
            extractDetailsWrapper.getChildren().removeIf(c -> !(c instanceof HBox));
            extractLinksWrapper.getChildren().removeIf(c -> !(c instanceof HBox));
            extractImagesWrapper.getChildren().removeIf(c -> !(c instanceof HBox));
            extractYoutubeWrapper.getChildren().removeIf(c -> !(c instanceof HBox));
            uploadGameWrapper.getChildren().removeIf(c -> !(c instanceof HBox));
            postFacebookWrapper.getChildren().removeIf(c -> !(c instanceof HBox));

            extractDetailsWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
            extractLinksWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
            extractImagesWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
            extractYoutubeWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
            uploadGameWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
            postFacebookWrapper.getChildren().addAll(getNewProgressIndicator(true), getDoneImageView(false), getWarningImageView(false));
        });
    }

    @FXML
    public void clickOnUpdateButton() {
        Thread processThread = new Thread(()->{
            if(!startAppInitializationProcess()) return;
            if(!startMainProcess()) {
                UIUtility.showErrorDialog("- Something wrong with updating");
            }
        });
        processThread.setDaemon(true);
        processThread.start();
    }


    @FXML
    public void clickOnBrowseButton() {
        String credentialPath = showFileChooserDialog();
        if(credentialPath != null) {
            YoutubeInitializer.addYoutubeCredentialEnv(credentialPath);
            ytCredentialPathLabel.setText(credentialPath);
        }
    }

    @FXML
    public void keyReleaseOnNoOfBrowser() {
        String value = browserField.getText();
        try {
            int intValue = Integer.parseInt(value);
            if(intValue<=0 || intValue>10) browserField.clear();
        } catch (Exception e) {
            browserField.clear();
        }
    }

    @FXML
    public void keyReleaseOnStartPageIndex() {
        String value = pageIndexField.getText();
        try {
            int intValue = Integer.parseInt(value);
            if(intValue<=0 || intValue>10000) pageIndexField.clear();
        } catch (Exception e) {
            pageIndexField.clear();
        }
    }

    private boolean startAppInitializationProcess() {
        boolean flag = true;
        try {
            if(fbCheckbox.isSelected()) {
                FacebookInitializer.initialize();
            }
            if(ytCheckbox.isSelected()) {
                YoutubeInitializer.initialize();
            }
        } catch (Exception ioe) {
            flag = false;
            UIUtility.showErrorDialog(ioe.getMessage());
        }
        return flag;
    }
    private boolean startMainProcess() {
        boolean flag = true;
        try {
            IggGameWebScraper iggGameWebScraper = new IggGameWebScraper(Integer.parseInt(browserField.getText()));
            iggGameWebScraper.setStartPageNumber(Integer.parseInt(pageIndexField.getText()));
            iggGameWebScraper.start();
        } catch (Exception e) {
            flag = false;
            UIUtility.showErrorDialog(e.getMessage());
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

    public void setPageIndex(int index) {
        Platform.runLater(()-> pageIndexText.setText(String.valueOf(index)));
    }
    public void setGameTitleText(String gameTitle) {
        Platform.runLater(()-> gameTitleLabel.setText(gameTitle));
    }
    public void setStatusText(String status) {
        Platform.runLater(()-> statusText.setText(status));
    }

    public ProgressIndicator getNewProgressIndicator(boolean visibility) {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setProgress(-1);
        progressIndicator.setPrefWidth(14);
        progressIndicator.setPrefHeight(13);
        progressIndicator.setStyle("-fx-progress-color: #00ad89;");
        progressIndicator.setVisible(visibility);
        return progressIndicator;
    }
    private ImageView getNewImageView(String imagePath, boolean visibility) {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(MainController.class.getClassLoader().getResource(imagePath)).toExternalForm()));
        imageView.setFitWidth(22);
        imageView.setFitHeight(16);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setVisible(visibility);
        return imageView;
    }
    public ImageView getDoneImageView( boolean visibility) {
        return getNewImageView("com/panda/gamelistautoupdater/img/app-icons/done.png", visibility);
    }
    public ImageView getWarningImageView( boolean visibility) {
        return getNewImageView("com/panda/gamelistautoupdater/img/app-icons/warning.png", visibility);
    }
    private void markProcessFinish(HBox wrapper, boolean isSuccess) {
        Platform.runLater(()->{
            wrapper.getChildren().remove(1);
            wrapper.getChildren().remove(isSuccess?2:1);
            wrapper.getChildren().get(1).setVisible(true);
        });
    }
    public void markExtractDetailFinish(boolean isSuccess) {markProcessFinish(extractDetailsWrapper, isSuccess);}
    public void markExtractLinksFinish(boolean isSuccess) {markProcessFinish(extractLinksWrapper, isSuccess);}
    public void markExtractImagesFinish(boolean isSuccess) {markProcessFinish(extractImagesWrapper, isSuccess);}
    public void markExtractYoutubeFinish(boolean isSuccess) {markProcessFinish(extractYoutubeWrapper, isSuccess);}
    public void markUploadGameFinish(boolean isSuccess) {markProcessFinish(uploadGameWrapper, isSuccess);}
    public void markPostFacebookFinish(boolean isSuccess) {markProcessFinish(postFacebookWrapper, isSuccess);}
}