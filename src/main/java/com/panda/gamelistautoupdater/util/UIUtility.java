package com.panda.gamelistautoupdater.util;

import com.panda.gamelistautoupdater.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class UIUtility {
    public static void showErrorDialog(String errorMessage) {
        showDialog("error-view.fxml");
    }

    public static void showDialog(String fxmlPath) {
        try {
            Stage fbStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Scene scene = null;
            scene = new Scene(fxmlLoader.load());
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
