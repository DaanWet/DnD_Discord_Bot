package Commands;

import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
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
            Message m = e.getMessage();
            if (args[0].matches("(?i)^D(ungeon)?M(aster)?$")){
                setRole(m, "DM");
            } else if (args[0].matches("(?i)^P(layers)?$")){
                setRole(m, "Player");
            } else if (args[0].matches("(?i)^D(ungeon)?M(aster)?Ch(annel)?$")){
                setChannel(m, "DMChannel");
            } else if (args[0].matches("(?i)^C(alend[ea]r)?Ch(annel)?$")){
                setChannel(m, "CalendarChannel");
            } else if (args[0].matches("(?i)^F(ood)?Ch(annel)?$")){
                setChannel(m, "FoodChannel");
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
            r = message.getGuild().getRoleById(roleID);
        } else if(message.getMentionedRoles().size() > 0){
            r = message.getMentionedRoles().get(0);
        }
        if (r != null){
            ConfigHandler configHandler = new ConfigHandler(message.getGuild());
            configHandler.setConfig(r.getId(), role);
            message.getChannel().sendMessage(String.format("Succesfully set the role for %s to %s", role, r.getName())).queue();
        } else {
            message.getChannel().sendMessage("You have to give a valid role (RoleID or mention)").queue();
        }
    }

    private void setChannel(Message message, String channel){
        String channelID = message.getContentRaw().split(" ")[2];
        TextChannel ch = null;
        if (isLong(channelID)){
            ch = message.getGuild().getTextChannelById(channelID);
        } else if (message.getMentionedChannels().size() > 0){
            ch = message.getMentionedChannels().get(0);
        }
        if (ch != null){
            ConfigHandler configHandler = new ConfigHandler(message.getGuild());
            configHandler.setConfig(ch.getId(), channel);
            message.getChannel().sendMessage(String.format("Succesfully set the %s to %s", channel, ch.getAsMention())).queue();
        } else {
            message.getChannel().sendMessage("You have to give a valid channel (ID or mention)").queue();
        }
    }
}
