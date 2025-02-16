package com.panda.gamelistautoupdater;

import com.panda.gamelistautoupdater.controllers.ControllerManipulator;
import com.panda.gamelistautoupdater.domains.scraping.IggGameWebScraper;
import com.panda.gamelistautoupdater.domains.upload.GameUploader;
import com.panda.gamelistautoupdater.domains.youtube.YoutubeDataFetcher;
import com.panda.gamelistautoupdater.exceptions.ChromeRelatedException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    private static double xOffset = 0;
    private static double yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ControllerManipulator.setMainController(fxmlLoader.getController());
        makeStageDraggable(stage, scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
//        GameUploader.upload(null,null,null,null,null);
        IggGameWebScraper iggGameWebScraper = new IggGameWebScraper(1);
        iggGameWebScraper.setStartPageNumber(1);
        try {
            iggGameWebScraper.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public static void makeStageDraggable(Stage stage, Scene scene) {
        scene.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}