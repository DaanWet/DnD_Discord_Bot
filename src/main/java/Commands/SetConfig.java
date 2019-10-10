package Commands;

import DataHandlers.ConfigHandler;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetConfig extends Command {

    private Map<String, String> regexRoleMap = Map.of(
            "DM", "(?i)^D(ungeon)?M(aster)?$",
            "Player", "(?i)^P(layers)?$"
            );
    private Map<String, String> regexChannelMap = Map.of(
            "DMChannel", "(?i)^D(ungeon)?M(aster)?Ch(annel)?$",
            "CalendarChannel", "(?i)^C(alend[ea]r)?Ch(annel)?$",
            "FoodChannel", "(?i)^F(ood)?Ch(annel)?$",
            "MemeChannel", "(?i)^M(eme)?Ch(annel)?$"
    );
    private String allconfigs;

    public SetConfig(){
        this.name = "setconfig";
        this.aliases = new String[]{"SC", "set", "config"};
        this.category = "Other";
        allconfigs = String.join(" ", Stream.concat(regexChannelMap.keySet().stream(), regexRoleMap.keySet().stream()).collect(Collectors.toList()));
    }
    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 2){
            boolean valid = false;
            Message m = e.getMessage();
            for (String key : regexRoleMap.keySet()){
                if (!valid && args[0].matches(regexRoleMap.get(key))){
                    setRole(m, key);
                    valid = true;
                }
            }
            for (String key : regexChannelMap.keySet()){
                if (!valid && args[0].matches(regexChannelMap.get(key))){
                    setChannel(m, key);
                    valid = true;
                }
            }
            if (!valid){
                e.getChannel().sendMessage("No setting named " + args[0] + "\nPossible settings:" + allconfigs).queue();
            }
        } else {
            e.getChannel().sendMessage("Possible settings: " + allconfigs).queue();
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
