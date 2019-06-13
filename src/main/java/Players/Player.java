package Players;


import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.awt.*;

public class Player extends Person {

    public Player(Guild g) {
        GuildController gc = g.getController();
        String playerId = new ConfigHandler(g).getPlayerRoleID();
        if (playerId.equals("0")){
            gc.createRole().setColor(Color.orange).setName("Dungeon Delvers").queue(role -> this.role = role);
            new ConfigHandler(g).setConfig(this.role.getId(), "Player");
        } else {
            this.role = g.getRoleById(playerId);
        }
    }
}
