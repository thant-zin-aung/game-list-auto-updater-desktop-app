package com.panda.gamelistautoupdater.controllers;

public class ControllerManipulator {
    private static MainController mainController;
    private static ErrorController errorController;

    public static void setMainController(MainController mainController) {
        ControllerManipulator.mainController = mainController;
    }

    public static void setErrorController(ErrorController errorController) {
        ControllerManipulator.errorController = errorController;
    }

    public static MainController getMainController() {
        return mainController;
    }

    public static ErrorController getErrorController() {
        return errorController;
    }
}
