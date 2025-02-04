package com.panda.gamelistautoupdater.initializers;

import com.panda.gamelistautoupdater.domains.youtube.YoutubeDataFetcher;

public class YoutubeInitializer {
    private static final String YOUTUBE_CREDENTIAL_ENV = "YOUTUBE_CREDENTIAL_PATH";
    public static boolean initialize() {
        boolean flag = false;
        if(System.getenv(YOUTUBE_CREDENTIAL_ENV)!=null) {
            try {
                YoutubeDataFetcher.fetch("testing");
                flag = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return flag;
    }
}
