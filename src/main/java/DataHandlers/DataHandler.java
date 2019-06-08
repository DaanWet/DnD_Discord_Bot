package DataHandlers;

import net.dv8tion.jda.core.entities.Guild;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Verwerkt de gegevens van een Guild en zet dit om naar een Java-verwerkbaar formaat.
 */
public class DataHandler {

    JSONObject jsonObject;
    protected String guild;

    @SuppressWarnings("unchecked")
    DataHandler(Guild g) {
        JSONParser parser = new JSONParser();
        guild = g.getId();

        try (FileReader reader = new FileReader("src/main/resources/Data.json")) {
            jsonObject = ((JSONObject) parser.parse(reader));
            if (!jsonObject.containsKey(g.getId())) {
                JSONArray food = new JSONArray();
                JSONArray dates = new JSONArray();
                JSONArray characters = new JSONArray();
                Map<String, String> configs = Map.of(
                        "DM", "0",
                        "Player", "0"
                );
                JSONObject config = new JSONObject(configs);
                JSONObject data = new JSONObject();
                data.put("Food", food);
                data.put("Dates", dates);
                data.put("Characters", characters);
                data.put("Config", config);
                jsonObject.put(g.getId(), data);
                save();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    void save() {
        try (FileWriter file = new FileWriter("src/main/resources/Data.json")) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
