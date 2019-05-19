import Commands.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class CommandListener extends ListenerAdapter {

    private Map<String, Command> dicecommands = Map.of(
            "d4", new Dice(4),
            "d6", new Dice(6),
            "d8", new Dice(8),
            "d10", new Dice(10),
            "d12", new Dice(12),
            "d20", new Dice(20),
            "d100", new Dice(100)

    );

    private Map<String, Command> calendarcommands = Map.of(
            "calendar", new Calendar(),
            "addsession", new AddSession(),
            "removesession", new RemoveSession()
    );


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].substring(0, 1).equals("/")) {
            String command = words[0].substring(1);
            if (words.length <= 2 && dicecommands.keySet().contains(command.toLowerCase())) {
                dicecommands.get(command).run(Arrays.copyOfRange(words, 1, words.length), e);
            } else if (calendarcommands.containsKey(command.toLowerCase())){
                calendarcommands.get(command).run(Arrays.copyOfRange(words, 1, words.length), e);
            }
            else if (words.length == 1 && (command.equalsIgnoreCase("commands") || command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h"))) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sumarville Commands");
                StringBuilder calendarcom = new StringBuilder();
                for (String c : calendarcommands.keySet()) {
                    calendarcom.append("\n/").append(c).append(": ").append(calendarcommands.get(c).getDescription());
                }
                calendarcom.delete(0, 1);
                eb.addField("Dice rolls", "/d4, /d6, /d8, /d10, /d12, /d20, /d100", true);
                eb.addField("Calendar", calendarcom.toString(), false);
                eb.setColor(Color.ORANGE);
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }
    }
}
