package Commands.NPCMessages;

import Commands.Command;
import DataHandlers.NPCMessageHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class RemoveMessage extends Command {

    public RemoveMessage(){
        this.name = "RemoveNPCMessage";
        this.category = "NPC-Messages";
        this.aliases = new String[]{"RemoveMessage", "RM"};
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1 && isIntegerOrLong(args[0])){
            NPCMessageHandler npch = new NPCMessageHandler(e.getGuild());
            ArrayList<Map<String, String>> messages = npch.getBasicMessages();
            int i = Integer.parseInt(args[0]);
            if(i < messages.size()){
                npch.removeMessage(i);
            }
        }
    }

    @Override
    public String getDescription() {
        return "Removes a message from the NPC-message list";
    }
}
