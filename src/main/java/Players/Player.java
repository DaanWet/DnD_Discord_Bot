package Players;


import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.awt.*;
import java.util.List;

public class Player extends Person {

    public Player(Guild g) {
        GuildController gc = g.getController();
        String playerId = new ConfigHandler(g).getPlayerRoleID();
        if (playerId.equals("0")){
            RoleAction role = gc.createRole().setColor(Color.orange).setName("Dungeon Delvers");
            this.role = role.complete();
            new ConfigHandler(g).setRoleID(this.role.getId(), "Player");
        } else {
            this.role = g.getRoleById(playerId);
        }
    }
}
