package com.panda.gamelistautoupdater.initializers;

import com.panda.gamelistautoupdater.domains.youtube.YoutubeDataFetcher;
import com.panda.gamelistautoupdater.util.CommandLine;
import com.panda.gamelistautoupdater.util.UIUtility;

public class YoutubeInitializer {
    public static final String YOUTUBE_CREDENTIAL_ENV = "YOUTUBE_CREDENTIAL_PATH";
    public static void initialize() throws Exception {
        if(System.getenv(YOUTUBE_CREDENTIAL_ENV)==null) {
            try {
                YoutubeDataFetcher.deleteRefreshTokenDirectory();
                YoutubeDataFetcher.fetch("testing");
            } catch (Exception e) {
                throw new Exception("""
                    - Something wrong with youtube api
                    - Maybe credentials path or file is wrong
                    - Or internet connection issue
                    [Response]: %s
                    """.formatted(e.getMessage()));
            }
        } else if (System.getenv(YOUTUBE_CREDENTIAL_ENV)!=null) {
            YoutubeDataFetcher.fetch("testing");
        }
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
