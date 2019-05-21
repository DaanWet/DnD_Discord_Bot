package Commands.Food;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodHandler {

    private JSONObject jsonObject;

    public FoodHandler() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("src/main/resources/Food.json")) {
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void addFood(String name, String emoji){
        JSONArray foods = (JSONArray) jsonObject.get("Food");
        Map<String, String> food = new HashMap<>();
        food.put("Name", name);
        food.put("Emoji", emoji);
        foods.add(new JSONObject(food));
        save();
    }

    public void removeFood(int i){
        JSONArray foods = (JSONArray) jsonObject.get("Food");
        foods.remove(i);
        save();
    }

    public ArrayList<Map<String, String>> getFood(){
        JSONArray jsonArray = (JSONArray) jsonObject.get("Food");
        ArrayList<Map<String, String>> food = new ArrayList<>();
        for (Object object : jsonArray){
            Map<String, String> f = new HashMap<>();
            JSONObject obj = (JSONObject) object;
            for (Object s : obj.keySet()){
                String name = (String) s;
                f.put(name, (String) obj.get(name));
            }
            food.add(f);
        }
        return food;
    }

    public int checkFood(String s){
        ArrayList<Map<String, String>> foods = getFood();
        boolean exists = false;
        int i = 0;
        while (!exists && i < foods.size()){
            if (foods.get(i).get("Name").equalsIgnoreCase(s)){
                exists = true;
            }
            i++;
        }
        if (exists){
            return i - 1;
        } else {
            return -1;
        }
    }

    public void save() {
        try (FileWriter file = new FileWriter("src/main/resources/Food.json")) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
