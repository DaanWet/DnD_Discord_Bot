package DataHandlers;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.json.simple.JSONObject;

public class ConfigHandler extends DataHandler{

    private JSONObject config;

    public ConfigHandler(Guild g) {
        super(g);
        config = (JSONObject) ((JSONObject) jsonObject.get(guild)).get("Config");
    }

    public String getDMRoleID(){
        return (String) config.get("DM");
    }
    public String getPlayerRoleID(){
        return (String) config.get("Player");
    }

    @SuppressWarnings("unchecked")
    public void setRoleID(String id, String role){
        Object key = config.replace(role, id);
        save();
    }
}
