import Dice.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

public class CommandListener extends ListenerAdapter {

    private Map<String, ArgsListener> commandMap = Map.of(
            "commands", new CommandLister(),
            "r", new DiceRoller()

    );


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].substring(0,1).equals("!")){
            String command = words[0].substring(1);
            if (commandMap.keySet().contains(command.toLowerCase())){
                ArgsListener listener = commandMap.get(command);
                listener.run(Arrays.copyOfRange(words, 1, words.length));
            }
        }
    }

    class CommandLister implements ArgsListener {
        @Override
        public void run(String[] args) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("DnD Commands");
            StringBuilder commands = new StringBuilder();
            for (String com : commandMap.keySet()){
                commands.append(", !");
                commands.append(com);
                commands.append(commandMap.get(com).description());
            }
            commands.delete(0, 1);
            eb.addField("Dice-rolls", commands.toString(), true);
            e.getChannel().sendMessage(eb.build()).queue();
        }

        @Override
        public String description() {
            return "Displays commands";
        }
    }
}
