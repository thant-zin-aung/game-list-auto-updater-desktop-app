package com.panda.gamelistautoupdater.domains.automations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.panda.gamelistautoupdater.domains.upload.GameUploader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.panda.gamelistautoupdater.exceptions.ChromeRelatedException;

import java.io.IOException;
import java.time.Duration;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class IggGameAutomateBrowser implements AutoCloseable {
    private final int MAX_BROWSER_COUNT = 10;
    private final WebDriver driver;
    public IggGameAutomateBrowser() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode (no UI)
        options.addArguments("--disable-gpu"); // Disable GPU hardware acceleration
        options.addArguments("--window-size=1920x1080"); // Set window size
        // Initialize WebDriver with ChromeOptions
        this.driver = new ChromeDriver(options);
    }

    public boolean checkGameAlreadyExist(String gameTitle) {
        boolean existFlag = false;
        String WEB_ROOT_URL = GameUploader.GAME_WEB_ROOT_URL;
        String gameExistCheckerUrl = WEB_ROOT_URL +"/admin_panel/exist-game-checker.php?game_title="+gameTitle;
        OkHttpClient client = new OkHttpClient();

        // Create a request
        Request request = new Request.Builder()
                .url(gameExistCheckerUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                existFlag = jsonObject.get("already_exist").getAsBoolean();
                System.out.println("Game Exist: " + existFlag);
            } else {
                System.err.println("Request failed with code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existFlag;
//        boolean isGameExist;
//        long startTime = System.nanoTime();
//        // Cause upload server do not accept ' in text...
//        gameTitle = gameTitle.replace("'","");
//        // Navigate to a website
//        driver.get(WEB_ROOT_URL+"/admin_panel/exist-game-checker.php?game_title="+gameTitle);
//
//        List<WebElement> preTags = driver.findElements(By.tagName("pre"));
//        if (preTags.isEmpty()) {
//            WebElement proceedLink = driver.findElement(By.id("proceed-link"));
//            WebElement advanceButton = driver.findElement(By.id("details-button"));
//            advanceButton.click();
//            proceedLink.click();
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(600));
//            WebElement newPagePreElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("pre")));
//            String tagValue = newPagePreElement.getText();
//            JsonObject jsonObject = JsonParser.parseString(tagValue).getAsJsonObject();
//            isGameExist = jsonObject.get("already_exist").getAsBoolean();
//            System.out.println("Tag value: " + tagValue);
//            System.out.println("Already exist: "+isGameExist);
//
//        } else {
//            String tagValue = preTags.get(0).getText();
//            JsonObject jsonObject = JsonParser.parseString(tagValue).getAsJsonObject();
//            isGameExist = jsonObject.get("already_exist").getAsBoolean();
//            System.out.println("Tag value: " + tagValue);
//            System.out.println("Already exist: "+isGameExist);
//        }
//
//        long endTime = System.nanoTime();
//        long elapsedTime = endTime - startTime;
//        double elapsedTimeInMilliseconds = elapsedTime / 1_000_000_000.0;
//        System.out.println("Processing time: ".concat(String.format("%.2f",elapsedTimeInMilliseconds)).concat("s"));
//        return isGameExist;
    }

    public List<String> getActualGameLinks(List<String> redirectLinks, int totalBrowser) throws ChromeRelatedException {
        if(totalBrowser > MAX_BROWSER_COUNT ) throw new ChromeRelatedException("Maximum browser per process is 10 and cannot be exceeded!");
        List<String> actualGameDownloadLinks = new LinkedList<>();
        int totalLink = redirectLinks.size();
        totalBrowser = Math.min(totalBrowser, totalLink);
        int totalLinkPerBrowser = totalLink/totalBrowser;
        System.out.println("Totallinkperbrowser: "+totalLinkPerBrowser);
        int startIndex = 0;
        for (int browserCount = 1 ; browserCount <= totalBrowser ; browserCount++) {
            List<String> seperatedRedirectLinks = new LinkedList<>();
            int endIndex = startIndex+totalLinkPerBrowser;
            if(browserCount==totalBrowser) {
                endIndex = redirectLinks.size();
            }
            for(int redirectCount = startIndex ; redirectCount < endIndex ; redirectCount++ ) {
                seperatedRedirectLinks.add(redirectLinks.get(redirectCount));
                startIndex++;
            }
            new Thread(() -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless"); // Run in headless mode (no UI)
                options.addArguments("--disable-gpu"); // Disable GPU hardware acceleration
                options.addArguments("--window-size=1920x1080"); // Set window size
                WebDriver driver = new ChromeDriver(options);
                for(int linkCount = 0 ; linkCount <= seperatedRedirectLinks.size()-1 ; linkCount++ ) {
                    driver.get(seperatedRedirectLinks.get(linkCount));
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10000));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nut")));
                    String gameRedirectURIValue = driver.findElement(By.cssSelector("form #url")).getDomAttribute("value");
                    System.out.println("Value: "+gameRedirectURIValue);
                    try {
                        actualGameDownloadLinks.add(Jsoup.connect("https://urlbluemedia.shop/get-url.php?url=".concat(gameRedirectURIValue)).get().baseUri());
                    } catch (IOException e) {
                        System.out.println("Error while connecting to https://urlbluemedia.shop/get-url.php");
                        System.out.println("Error: "+e.getMessage());
                    }
                }
                driver.quit();
            }).start();
        }
        while(actualGameDownloadLinks.size() != totalLink) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        actualGameDownloadLinks.sort(Comparator.comparing(o -> o.substring(o.length() - 9)));
        return actualGameDownloadLinks;
    }

    public void closeBrowser() {
        // Close the browser
        driver.quit();
    }

    @Override
    public void close() {
        // Close the browser
        driver.quit();
    }
}
