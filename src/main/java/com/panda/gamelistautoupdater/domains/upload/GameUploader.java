package com.panda.gamelistautoupdater.domains.upload;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GameUploader {
    public static final String GAME_WEB_ROOT_URL = "https://blackskypcgamestore.x10.mx";

    public static JSONObject createResponse(
            String gameTitle,
            String developer,
            String publisher,
            List<String> genreList,
            List<Map<String, String>> specificationList,
            List<String> downloadLinkList,
            List<String> gamePlayImagesList,
            String youtubeTrailerLink) {

        JSONObject response = new JSONObject();
        response.put("gameTitle", gameTitle);
        response.put("developer", developer);
        response.put("publisher", publisher);
        response.put("genreList", new JSONArray(genreList));
        response.put("specificationList", new JSONArray(specificationList));
        response.put("downloadLinkList", String.join("\n", downloadLinkList));
        response.put("gamePlayImagesList", String.join("\n", gamePlayImagesList));
        response.put("youtubeTrailerLink", youtubeTrailerLink);
        response.put("downloadSize", downloadLinkList.size() == 1 ? 3 : downloadLinkList.size()*5);

        return response;
    }


    public static boolean upload(String gameTitle, String developer, String publisher, List<String> genreList, List<Map<String, String>> specificationList, List<String> downloadLinkList, List<String> gamePlayImagesList, String youtubeTrailerLink) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JSONObject jsonObject = createResponse(gameTitle, developer, publisher, genreList, specificationList, downloadLinkList, gamePlayImagesList, youtubeTrailerLink);
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("http://localhost/pc_game_store/admin_panel/update-game.php")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            assert response.body() != null;
            System.out.println("Response: " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
