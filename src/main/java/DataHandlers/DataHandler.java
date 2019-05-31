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

public class DataHandler {

    protected JSONObject jsonObject;
    protected String guild;

    @SuppressWarnings("unchecked")
    public DataHandler(Guild g) {
        JSONParser parser = new JSONParser();
        guild = g.getId();
        try (FileReader reader = new FileReader("src/main/resources/Data.json")) {
            jsonObject = ((JSONObject) parser.parse(reader));
            if (!jsonObject.containsKey(g.getId())) {
                JSONArray food = new JSONArray();
                JSONArray dates = new JSONArray();
                JSONObject data = new JSONObject();
                data.put("Food", food);
                data.put("Dates", dates);
                jsonObject.put(g.getId(), data);
                save();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try (FileWriter file = new FileWriter("src/main/resources/Data.json")) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
