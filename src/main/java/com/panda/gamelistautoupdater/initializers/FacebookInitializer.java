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
    private static String pageId;
    public static void initialize() throws Exception {
        if(System.getenv(FacebookHandler.ENV_FB_PAGE_TOKEN_KEY)==null || FacebookHandler.isTokenExpired()) {
            UIUtility.showFacebookCredentialsDialog();
        }
    }

    public static void setCredentials(String appId, String appSecret, String appScopeUserId, String pageId, String shortLivePageToken) {
        FacebookInitializer.appId = appId;
        FacebookInitializer.appSecret = appSecret;
        FacebookInitializer.appScopeUserId = appScopeUserId;
        FacebookInitializer.pageId = pageId;
        FacebookInitializer.shortLivePageToken = shortLivePageToken;
        FacebookHandler.extendPageAccessToken(appId, appSecret, appScopeUserId, pageId, shortLivePageToken);
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

    public static String getPageId() {
        return pageId;
    }

    public String getShortLivePageToken() {
        return shortLivePageToken;
    }
}
