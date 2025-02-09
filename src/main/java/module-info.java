module com.panda.gamelistautoupdater {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires org.seleniumhq.selenium.api;
    requires org.seleniumhq.selenium.chrome_driver;
    requires org.seleniumhq.selenium.support;
    requires com.google.gson;
    requires okhttp3;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.jackson2;
    requires google.api.services.youtube.v3.rev222;
    requires jdk.httpserver;


    opens com.panda.gamelistautoupdater to javafx.fxml;
    exports com.panda.gamelistautoupdater;
    exports com.panda.gamelistautoupdater.controllers;
    opens com.panda.gamelistautoupdater.controllers to javafx.fxml;
    exports com.panda.gamelistautoupdater.initializers;
    opens com.panda.gamelistautoupdater.initializers to javafx.fxml;
}