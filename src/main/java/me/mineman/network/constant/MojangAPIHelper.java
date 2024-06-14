package me.mineman.network.constant;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

public class MojangAPIHelper {

    public static Logger logger;

    private final static int[] DASH_SPOTS = new int[]{8, 13, 18, 23};

    public static Optional<UUID> getUUIDFromUsername(String username) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                String shortUUID = jsonObject.get("id").getAsString();
                StringBuilder sb = new StringBuilder(shortUUID);
                for (int spot : DASH_SPOTS) {
                    sb.insert(spot, '-');
                }
                String uuid = sb.toString();
                return Optional.of(UUID.fromString(uuid));
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return Optional.empty();
            } else {
                logger.info("HTTP GET request failed: " + responseCode);
                return Optional.empty();
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
