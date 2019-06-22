package Commands.NPCMessages;

import Commands.Command;
import DataHandlers.NPCMessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class ShowMessages extends Command {

    public ShowMessages(){
        this.name = "ShowMessages";
        this.aliases = new String[]{"SM", "Messages"};
        this.category = "NPC-Messages";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 0){
            NPCMessageHandler npcmh = new NPCMessageHandler(e.getGuild());
            EmbedBuilder eb = new EmbedBuilder();
            ArrayList<Map<String,String>> messages = npcmh.getBasicMessages();
            eb.setTitle("Basic NPC messages");
            for (Map<String, String> message : messages){
                String name = message.get("npc");
                eb.addField(!name.equals("")? name : "Sumarville", message.get("message"), false);
            }
            e.getChannel().sendMessage(eb.build()).queue();
        }
    }

    @Override
    public String getDescription() {
        return "Shows the basic messages";
    }
}
