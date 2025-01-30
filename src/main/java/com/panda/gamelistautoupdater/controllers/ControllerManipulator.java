package com.panda.gamelistautoupdater.controllers;

public class ControllerManipulator {
    private static ErrorController errorController;
    public static void setErrorController(ErrorController errorController) {
        ControllerManipulator.errorController = errorController;
    }

    public static ErrorController getErrorController() {
        return errorController;
    }
}
