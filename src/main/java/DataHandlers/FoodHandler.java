package DataHandlers;

import net.dv8tion.jda.core.entities.Guild;
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

public class FoodHandler extends DataHandler{

    private JSONArray food;


    public FoodHandler(Guild g) {
        super(g);
        food = (JSONArray)((JSONObject) jsonObject.get(guild)).get("Food");
    }
    @SuppressWarnings("unchecked")
    public void addFood(String name, String emoji){
        Map<String, String> foodMap = new HashMap<>();
        foodMap.put("Name", name);
        foodMap.put("Emoji", emoji);
        food.add(new JSONObject(foodMap));
        save();
    }

    public void removeFood(int i){
        food.remove(i);
        save();
    }

    public ArrayList<Map<String, String>> getFood(){
        ArrayList<Map<String, String>> foodList = new ArrayList<>();
        for (Object object : food){
            Map<String, String> f = new HashMap<>();
            JSONObject obj = (JSONObject) object;
            for (Object s : obj.keySet()){
                String name = (String) s;
                f.put(name, (String) obj.get(name));
            }
            foodList.add(f);
        }
        return foodList;
    }

    public int checkFood(String emoji){
        ArrayList<Map<String, String>> foods = getFood();
        boolean exists = false;
        int i = 0;
        while (!exists && i < foods.size()){
            if (foods.get(i).get("Emoji").equalsIgnoreCase(emoji)){
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
}
