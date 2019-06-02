import Commands.Calendar.AddSession;
import Commands.Calendar.Calendar;
import Commands.Calendar.RemoveSession;
import Commands.Command;
import Commands.Dice;
import Commands.Food.AddFood;
import Commands.Food.GetFood;
import Commands.Food.RemoveFood;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.*;

/**
 * Luisteraar die luistert naar commando's
 */
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

    private ArrayList<Command> commands = new ArrayList<>(Arrays.asList(new Calendar(), new AddSession(), new RemoveSession(), new AddFood(), new GetFood(), new RemoveFood()));
    private ArrayList<Command> testcommands = new ArrayList<>(Collections.singletonList(new DungeonMaster()));


    /**
     * Deze methode checkt of een bericht een geldig commanda is en als dit zo is checkt het of het een dicecommand is, een hulp command of een complex (aka 1tje van meerdere woroden) command.
     * -> Dicecommand: Gooi de desbetreffende dice x keer
     * -> Hulp command: Bouw de hulpboodschap op en toon deze
     * -> Complex command: Check of er woord daaruit een commando is en voer deze dan uit.
     * @param e Event die getrowed wordt iedere keer dat er een bericht verstuurd wordt
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].charAt(0) == '/') {
            String command = words[0].substring(1);
            if (words.length <= 2 && dicecommands.keySet().contains(command.toLowerCase())) {
                dicecommands.get(command).run(Arrays.copyOfRange(words, 1, words.length), e);
                // Matches(regex) -> (?i) is voor case insensitive
            } else if (words.length == 1 && command.matches("(?i)^(commands|help|h)$")) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sumarville Commands");
                Map<String, StringBuilder> sbs = new HashMap<>();
                for (Command c : commands) {
                    String cat = c.getCategory();
                    if (!sbs.containsKey(cat)) {
                        sbs.put(cat, new StringBuilder());
                    }
                    StringBuilder sb = sbs.get(cat);
                    sb.append(String.format("\n/%s[", c.getName()));
                    sb.append("\n/").append(c.getName()).append("[");
                    Arrays.stream(c.getAliases()).forEach(alias -> sb.append(alias).append(", "));
                    sb.delete(sb.length() - 2, sb.length()).append(String.format("]: %s", c.getDescription()));
                }
                eb.addField("Dice rolls", "/d4, /d6, /d8, /d10, /d12, /d20, /d100", true);
                eb.setColor(Color.ORANGE);
                sbs.keySet().forEach(s -> eb.addField(s, sbs.get(s).toString().trim(), false));
                e.getChannel().sendMessage(eb.build()).queue();
            } else {
                int ctr = 0;
                while (ctr < commands.size() && !commands.get(ctr).isCommandFor(command)) {
                    ctr++;
                }

                if (ctr < commands.size()) {
                    commands.get(ctr).run(Arrays.copyOfRange(words, 1, words.length), e);
                } else {
                    for (Command c : testcommands) {
                        if (c.isCommandFor(command)) {
                            c.run(Arrays.copyOfRange(words, 1, words.length), e);
                        }
                    }
                }
            }
        }
    }
}
