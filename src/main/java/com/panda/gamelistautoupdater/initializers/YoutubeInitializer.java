package com.panda.gamelistautoupdater.initializers;

import com.panda.gamelistautoupdater.domains.youtube.YoutubeDataFetcher;
import com.panda.gamelistautoupdater.util.CommandLine;

public class YoutubeInitializer {
    public static final String YOUTUBE_CREDENTIAL_ENV = "YOUTUBE_CREDENTIAL_PATH";
    public static boolean initialize() {
        boolean flag = false;
        if(System.getenv(YOUTUBE_CREDENTIAL_ENV)!=null) {
            try {
                YoutubeDataFetcher.deleteRefreshTokenDirectory();
                YoutubeDataFetcher.fetch("testing");
                flag = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return flag;
    }

    public static void addYoutubeCredentialEnv(String credentialPath) {
        try {
            CommandLine.i().getResultOfExecution("setx "+YOUTUBE_CREDENTIAL_ENV+" "+credentialPath);
        } catch (Exception e) {
            System.out.println("Failed to add youtube credentials.json path to environment...");
            System.out.println("Error: "+e.getMessage());
        }
    }
}
