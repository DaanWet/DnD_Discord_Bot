package Commands;

import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SetConfig extends Command {

    public SetConfig(){
        this.name = "setconfig";
        this.aliases = new String[]{"SC", "set", "config"};
        this.category = "Other";
    }
    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 2){
            if (args[0].matches("(?i)^D(ungeon)?M(aster)?$")){
                setRole(e.getMessage(), "DM");
            } else if (args[0].matches("(?i)^P(layers)?$")){
                setRole(e.getMessage(), "Player");
            }
        }
    }

    @Override
    public String getDescription() {
        return "Sets up the configuration for Sumarville";
    }

    private void setRole(Message message, String role){
        String roleID = message.getContentRaw().split(" ")[2];
        Role r = null;
        if (isLong(roleID)) {
            try{
                r = message.getGuild().getRoleById(roleID);
            } catch (Exception e){
                message.getChannel().sendMessage("error").queue();
            }
        } else if(message.getMentionedRoles().size() > 0){
            r = message.getMentionedRoles().get(0);
        }
        if (r != null){
            ConfigHandler configHandler = new ConfigHandler(message.getGuild());
            configHandler.setRoleID(r.getId(), role);
            message.getChannel().sendMessage(String.format("Succesfully set the role for %s to %s", role, r.getName())).queue();
        } else {
            message.getChannel().sendMessage("You have to give a valid role (RoleID or mention)").queue();
        }
    }
}
