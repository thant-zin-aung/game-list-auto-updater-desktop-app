package com.panda.gamelistautoupdater.initializers;

import com.panda.gamelistautoupdater.domains.facebook.FacebookHandler;
import com.panda.gamelistautoupdater.util.UIUtility;

import java.io.IOException;

public class FacebookInitializer {

    public static void initialize() {
        try {
            if(System.getenv(FacebookHandler.ENV_FB_PAGE_TOKEN_KEY)==null || FacebookHandler.isTokenExpired()) {
                UIUtility.showFacebookCredentialsDialog();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
