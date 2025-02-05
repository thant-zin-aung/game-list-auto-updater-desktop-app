package com.panda.gamelistautoupdater.initializers;

import com.panda.gamelistautoupdater.domains.facebook.FacebookHandler;
import com.panda.gamelistautoupdater.util.UIUtility;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FacebookInitializer {
    private static String appId;
    private static String appSecret;
    private static String appScopeUserId;
    private static String shortLivePageToken;
    public static void initialize() {
        try {
            if(System.getenv(FacebookHandler.ENV_FB_PAGE_TOKEN_KEY)==null || FacebookHandler.isTokenExpired()) {
                UIUtility.showFacebookCredentialsDialog();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCredentials(String appId, String appSecret, String appScopeUserId, String shortLivePageToken) {
        FacebookInitializer.appId = appId;
        FacebookInitializer.appSecret = appSecret;
        FacebookInitializer.appScopeUserId = appScopeUserId;
        FacebookInitializer.shortLivePageToken = shortLivePageToken;
        FacebookHandler.extendPageAccessToken(appId, appSecret, appScopeUserId, shortLivePageToken);
    }

    public static boolean isAllCredentialsSet() {
        return appId!=null && appSecret!=null && appScopeUserId!=null && shortLivePageToken!=null;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getAppScopeUserId() {
        return appScopeUserId;
    }

    public String getShortLivePageToken() {
        return shortLivePageToken;
    }
}
