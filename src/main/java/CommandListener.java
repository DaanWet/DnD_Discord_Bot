import Dice.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.annotation.processing.SupportedSourceVersion;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.Function;

public class CommandListener extends ListenerAdapter {

    private Map<String, Dice> dices = Map.of(
            "d4", new Dice(4),
            "d6", new Dice(6),
            "d8", new Dice(8),
            "d10", new Dice(10),
            "d12", new Dice(12),
            "d20", new Dice(20),
            "d100", new Dice(100)
    );

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].substring(0, 1).equals("/")) {
            String command = words[0].substring(1);
            if (dices.keySet().contains(command.toLowerCase()) && words.length <= 2) {
                EmbedBuilder eb = new EmbedBuilder();
                Dice dice = dices.get(command.toLowerCase());
                String name = (e.getMember().getNickname() != null) ? e.getMember().getNickname() : e.getMember().getEffectiveName();
                if (words.length == 1) {
                    eb.addField(name + " rolled a " + command.toLowerCase() + " and got: ", Integer.toString(dice.roll()), true);
                } else {
                    if (words[1].equalsIgnoreCase("adv")) {
                        int first = dice.roll();
                        int second = dice.roll();
                        eb.addField(name + " rolled a " + command.toLowerCase() + " with advantage and got:", first + " & " + second + " => " + Math.max(first, second), true);
                    } else if (words[1].equalsIgnoreCase("dis")) {
                        int first = dice.roll();
                        int second = dice.roll();
                        eb.addField(name + " rolled a " + command.toLowerCase() + " with disadvantage and got:", first + " & " + second + " => " + Math.min(first, second), true);
                    } else if (isInteger(words[1])) {
                        int aantal = Integer.parseInt(words[1]);
                        if (aantal <= 50) {
                            int som = 0;
                            StringBuilder result = new StringBuilder();
                            int roll = dice.roll();
                            result.append(roll);
                            som += roll;
                            for (int i = 1; i < aantal; i++) {
                                result.append(" + ");
                                roll = dice.roll();
                                result.append(roll);
                                som += roll;
                            }
                            result.append(" = ");
                            result.append(som);
                            eb.addField(name + " rolled " + aantal + " times a " + command.toLowerCase() + " and got:", result.toString(), true);
                        } else {
                            eb.setTitle("We currently do not support more than 50 rolls in one command");
                            e.getChannel().sendMessage(eb.build()).queue();
                            return;
                        }
                    }
                }
                eb.setColor(Color.ORANGE);
                eb.setThumbnail(dice.getImage());
                e.getChannel().sendMessage(eb.build()).queue();
            } else if (words.length == 1 && (command.equalsIgnoreCase("commands") || command.equalsIgnoreCase("help") || command.equalsIgnoreCase("h"))) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sumarville Commands");
                StringBuilder dicerolls = new StringBuilder();
                for (String dice : dices.keySet()) {
                    dicerolls.append(", /");
                    dicerolls.append(dice);
                }
                dicerolls.delete(0, 1);
                eb.addField("Dice-rolls", dicerolls.toString(), true);
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }
    }

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
