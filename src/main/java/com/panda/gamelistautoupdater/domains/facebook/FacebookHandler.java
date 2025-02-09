package com.panda.gamelistautoupdater.domains.facebook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.panda.gamelistautoupdater.controllers.ControllerManipulator;
import com.panda.gamelistautoupdater.util.UIUtility;
import okhttp3.*;
import com.panda.gamelistautoupdater.util.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FacebookHandler {
    public static final String ENV_FB_PAGE_TOKEN_KEY = "fb_long_live_page_access_token";
    public static final String ENV_FB_PAGE_ID = "fb_page_id";
    private static final String BASE_URL = "https://graph.facebook.com/v21.0";

    private static boolean post(String message, String imagePath, boolean isImageUrl) throws IOException {
        ControllerManipulator.getMainController().setStatusText("Uploading game info to facebook page");
        String endPointUrl = BASE_URL+"/"+System.getenv(ENV_FB_PAGE_ID)+"/photos";

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("access_token", System.getenv(ENV_FB_PAGE_TOKEN_KEY))
                .addFormDataPart("message", message);
        if(isImageUrl) {
           builder.addFormDataPart("url", imagePath);
        } else {
            File imageFile = new File(imagePath);
            RequestBody imageBody = RequestBody.create(imageFile, MediaType.parse("image/jpeg"));
            builder.addFormDataPart("source", imageFile.getName(), imageBody);
        }
        RequestBody textBody = builder.build();

        Request request = new Request.Builder()
                .url(endPointUrl) // Replace with your endpoint
                .post(textBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Response: " + response.body().string());
                return true;
            } else {
                throw new IOException("""
                        - Failed to POST
                        - [Response]: %s
                        """.formatted(response.body().string()));
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public static boolean postWithUrl(String message, String imageUrl) throws IOException {
        return post(message, imageUrl, true);
    }
    public static boolean postWithFilePath(String message, String imageFilePath) throws IOException {
        return post(message, imageFilePath, false);
    }

    public static boolean isTokenExpired() throws IOException {
        String pageToken = System.getenv(ENV_FB_PAGE_TOKEN_KEY);
        if(pageToken==null) {
            return true;
        }
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/me")).newBuilder()
                .addQueryParameter("access_token", pageToken)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) return false;
            else {
                assert response.body() != null;
                throw new IOException(response.body().string());
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private static String getLongLivedUserToken(String appId, String appSecret, String shortLivedToken) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = HttpUrl.parse(BASE_URL + "/oauth/access_token").newBuilder()
                .addQueryParameter("grant_type", "fb_exchange_token")
                .addQueryParameter("client_id", appId)
                .addQueryParameter("client_secret", appSecret)
                .addQueryParameter("fb_exchange_token", shortLivedToken)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            System.out.println("long live user token: "+jsonObject.get("access_token").getAsString());
            return jsonObject.get("access_token").getAsString();
        }
    }

    private static String getPageAccessToken(String appScopeUserId, String longLivedUserToken) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(BASE_URL + "/"+appScopeUserId+"/accounts").newBuilder()
                .addQueryParameter("access_token", longLivedUserToken)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return response.body().string(); // JSON response with the page access token
        }
    }

    private static void addFacebookEnvironmentValue(String longLivePageAccessToken, String pageId) {
        try {
            CommandLine.i().getResultOfExecution("setx "+ENV_FB_PAGE_TOKEN_KEY+" "+longLivePageAccessToken);
            CommandLine.i().getResultOfExecution("setx "+ENV_FB_PAGE_ID+" "+pageId);
        } catch (Exception e) {
            System.out.println("Failed to add fb long live page access key to environment...");
            System.out.println("Error: "+e.getMessage());
        }
    }

    public static void extendPageAccessToken(String appId, String appSecret, String appScopeUserId, String pageId, String shortLivePageAccessToken) {

        try {
            // Get Long-Lived User Token
            String longLivedUserToken = getLongLivedUserToken(appId, appSecret, shortLivePageAccessToken);
            // Get Page Access Token
            String pageAccessTokenJson = getPageAccessToken(appScopeUserId, longLivedUserToken);
            System.out.println("Page Access Token Response: " + pageAccessTokenJson);

            JsonObject jsonObject = JsonParser.parseString(pageAccessTokenJson).getAsJsonObject();
            String longLivePageAccessToken = jsonObject.get("data").getAsJsonArray().get(0).getAsJsonObject().get("access_token").getAsString();
            System.out.println("Extracted page long live access token: "+ jsonObject.get("data").getAsJsonArray().get(0).getAsJsonObject().get("access_token").getAsString());

            addFacebookEnvironmentValue(longLivePageAccessToken, pageId);

        } catch (IOException e) {
            UIUtility.showErrorDialog("""
                            - Facebook credentials are not fully set
                            - Or credentials are expired/incorrect
                            - Or cannot connect to facebook
                            [Api message] - %s""".formatted(e.getMessage()));
        }
    }
}
