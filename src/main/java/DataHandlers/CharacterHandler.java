package DataHandlers;

import net.dv8tion.jda.core.entities.Guild;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CharacterHandler extends DataHandler {

    private JSONArray characters;

    public CharacterHandler(Guild g) {
        super(g);
        characters = (JSONArray) ((JSONObject) jsonObject.get(guild)).get("Characters");
    }
    @SuppressWarnings("unchecked")
    public void addCharacter(Map<String, String> character){
        characters.add(new JSONObject(character));
        save();
    }
    @SuppressWarnings("unchecked")
    public Map<String, String> getCharacter(String name, String type){
        Map<String, String> characterSheet = null;
        int index = getIndex(name, type);
        if (index != -1){
            characterSheet = (JSONObject) characters.get(index);
        }
        return characterSheet;
    }

    public void removeCharacter(String name, String type){
        int index = getIndex(name, type);
        characters.remove(index);
        save();
    }

    private int getIndex(String name, String type){
        boolean found = false;
        int i = 0;
        while (!found && i < characters.size()){
            JSONObject character = (JSONObject) characters.get(i);
            if (character.get(type).equals(name)){
                found = true;
            } else {
                i ++;
            }
        }
        return found? i : -1;
    }

}
