package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class NameHistory {

    private static @Getter @Setter NameHistory instance;

    public NameHistory() {
        setInstance(this);
    }

    /**
     * Get the UUID of a player by their username.
     * @param playername Minecraft player's name.
     * @return The player's UUID as a string.
     * @throws IOException If there is an error fetching data from the API.
     */
    public String getUUID(String playername) throws IOException {
        String urlStr = "https://api.mojang.com/users/profiles/minecraft/" + playername;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlStr).openStream()))) {
            String response = in.readLine();
            if (response != null && response.contains("{\"id\":")) {
                String uuid = response.toLowerCase()
                        .replace("{\"id\":", "")
                        .replace("\"", "")
                        .replace(",", "")
                        .replace("legacy:true", "")
                        .replace("name:" + playername.toLowerCase() + "}", "");
                return formatUUID(uuid);
            }
        }
        return null; // Return null if not found
    }

    /**
     * Format a UUID string into the standard 8-4-4-4-12 format.
     * @param uuid The raw UUID string.
     * @return The formatted UUID string.
     */
    private String formatUUID(String uuid) {
        return uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-"
                + uuid.substring(16, 20) + "-" + uuid.substring(20, 32);
    }

    /**
     * Get the list of previous names for a player.
     * @param playername Minecraft player's name.
     * @return A list of previous player names.
     * @throws IOException If there is an error fetching data from the API.
     */
    public List<String> getNames(String playername) throws IOException {
        String str = getPrevNames(playername);
        if (str == null) {
            Logger.WARNING.log("No name history found for player: " + playername);
            return new ArrayList<>(); // Return empty list if no names found
        }

        JsonArray jsonArray = JsonParser.parseString(str).getAsJsonArray();
        List<String> names = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            names.add(element.getAsJsonObject().get("name").getAsString());
        }
        return names;
    }

    /**
     * Get the full name history of a player.
     * @param playername Minecraft player's name.
     * @return The raw JSON response from Mojang's API containing name history.
     * @throws IOException If there is an error fetching data from the API.
     */
    public String getPrevNames(String playername) throws IOException {
        String uuid = getUUID(playername);
        if (uuid == null) {
            return null;
        }

        String target = String.format("https://api.mojang.com/user/profiles/%s/names", uuid.replace("-", ""));
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(target).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.connect();

            if (connection.getResponseCode() != 200) {
                Logger.WARNING.log("Failed to fetch name history for UUID: " + uuid);
                return null;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line).append('\n');
                }
                return response.toString();
            }
        } catch (IOException e) {
            Logger.ERROR.log("Error fetching name history: " + e.getMessage());
            throw e; // Re-throw the exception after logging
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
