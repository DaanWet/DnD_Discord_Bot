package Players;

import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.awt.*;


public class DM extends Person {

    public DM(Guild g){
        GuildController guildController = g.getController();
        String DmID = new ConfigHandler(g).getDMRoleID();
        if (DmID.equals("0")) {
            guildController.createRole().setColor(Color.MAGENTA).setName("DM").queue(role1 -> this.role = role1);
            new ConfigHandler(g).setConfig(this.role.getId(), "DM");
        } else {
            this.role = g.getRoleById(DmID);
        }

    }
}
