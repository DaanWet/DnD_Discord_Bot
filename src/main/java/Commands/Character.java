package Commands;

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
        messageListener = l;
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 0){
            messageListener.addBuilder(new CharacterSheetBuilder(e.getChannel(), e.getMember(), messageListener));
        }
    }

    @Override
    public String getDescription() {
        return "Starts the creation of a charactersheet";
    }
}
