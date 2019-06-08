package Commands.Users;

import Commands.Command;
import DataHandlers.CharacterHandler;
import Listeners.MessageListener;
import Players.CharacterSheet.CharacterSheetBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Map;

public class Character extends Command {

    private MessageListener messageListener;

    public Character(MessageListener l){
        this.name = "CharacterSheet";
        this.aliases = new String[]{"CS", "charsheet"};
        this.category = "Players/Users";
        messageListener = l;
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 0){
            messageListener.addBuilder(new CharacterSheetBuilder(e.getChannel(), e.getMember(), messageListener));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")){
            CharacterHandler c = new CharacterHandler(e.getGuild());
            Map<String, String> character = c.getCharacter(args[1], "name");
            if (character != null){
                c.removeCharacter(args[1], "name");
                e.getChannel().sendMessage(String.format("Succesfully removed %s", args[1])).queue();
            } else {
                e.getChannel().sendMessage("Usage: /CharacterSheet <remove name>").queue();
            }
        } else {
            e.getChannel().sendMessage("Usage: /CharacterSheet <remove name>").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Starts the creation of a charactersheet";
    }
}
