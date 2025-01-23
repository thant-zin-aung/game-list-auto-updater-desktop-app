package com.panda.gamelistautoupdater;

import com.panda.gamelistautoupdater.domains.automations.IggGameAutomateBrowser;
import com.panda.gamelistautoupdater.exceptions.ChromeRelatedException;

import java.io.IOException;

public class TestMain {
    public static void main(String[] args) throws IOException, ChromeRelatedException {
//        System.out.println(Initializer.initialize() ? "Ready to use..." : "Failed to use");
//        IggGameWebScraper iggGameWebScraper = new IggGameWebScraper(2);
//        iggGameWebScraper.start();

        IggGameAutomateBrowser iggGameAutomateBrowser = new IggGameAutomateBrowser();
        iggGameAutomateBrowser.checkGameAlreadyExist("little nightmaer");

    }
}