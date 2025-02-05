package com.panda.gamelistautoupdater.util;

import com.panda.gamelistautoupdater.Main;
import com.panda.gamelistautoupdater.controllers.ControllerManipulator;
import com.panda.gamelistautoupdater.controllers.ErrorController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UIUtility {
    public static void showErrorDialog(String errorMessage) {
        showDialog("error-view.fxml");
        ControllerManipulator.getErrorController().setErrorMessage(errorMessage);
    }

    public static void showFacebookCredentialsDialog() {
        showDialog("fb-credentials-view.fxml");
    }

    public static void showDialog(String fxmlPath) {
        try {
            Stage fbStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            if(fxmlLoader.getController() instanceof ErrorController && ControllerManipulator.getErrorController() == null) {
                ControllerManipulator.setErrorController(fxmlLoader.getController());
            }
            Main.makeStageDraggable(fbStage, scene);
            scene.setFill(Color.TRANSPARENT);
            fbStage.initStyle(StageStyle.UNDECORATED);
            fbStage.initModality(Modality.APPLICATION_MODAL);
            fbStage.setScene(scene);
            fbStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
