package Players;

import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.RoleAction;

import java.awt.*;
import java.util.List;


public class DM extends Person {

    public DM(Guild g){
        GuildController guildController = g.getController();
        String DmID = new ConfigHandler(g).getDMRoleID();
        if (DmID.equals("0")) {
            RoleAction role = guildController.createRole().setColor(Color.MAGENTA).setName("DM");
            this.role = role.complete();
            new ConfigHandler(g).setRoleID(this.role.getId(), "DM");
        } else {
            this.role = g.getRoleById(DmID);
        }

    }
}
