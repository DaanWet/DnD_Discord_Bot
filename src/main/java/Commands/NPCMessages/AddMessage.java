package Commands.NPCMessages;

import Commands.Command;
import DataHandlers.CharacterHandler;
import DataHandlers.NPCMessageHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class AddMessage extends Command {

    public AddMessage(){
        this.name = "AddNPCMessage";
        this.category = "NPC-Messages";
        this.aliases = new String[]{"AddMessage", "Am"};
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length > 0){
            StringBuilder sb = new StringBuilder();
            for (String arg: args){
                sb.append(arg).append(" ");
            }
            String[] words = sb.toString().trim().split(": ", 2);
            System.out.println(words[0]);
            CharacterHandler chh = new CharacterHandler(e.getGuild());
            ArrayList<String> names = chh.getAllCharacterNames(true);
            String name = "";
            String message = words[0];
            if (words.length > 1){
                name = words[0];
                message = words[1];
            }
            new NPCMessageHandler(e.getGuild()).addMessage(message, "Basic", name);
            e.getChannel().sendMessage(String.format("Sucesfully added '%s' to the NPC-message list", message)).queue();
        }
    }

    @Override
    public String getDescription() {
        return "Adds a NPC-message";
    }
}
