import Dice.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.Function;

public class CommandListener extends ListenerAdapter {

    private Map<String, Dice> dices = Map.of(
            "d4", new D4(),
            "d6", new D6(),
            "d8", new D8(),
            "d10", new D10(),
            "d12", new D12(),
            "d20", new D20(),
            "d100", new D100()
    );

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].substring(0, 1).equals("!")) {
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
                        int som = 0;
                        int aantal = Integer.parseInt(words[1]);
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
                    }
                }
                eb.setThumbnail(dice.getImage());
                e.getChannel().sendMessage(eb.build()).queue();
            } else if (command.equalsIgnoreCase("commands") && words.length == 1) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sumarville Commands");
                StringBuilder dicerolls = new StringBuilder();
                for (String dice : dices.keySet()) {
                    dicerolls.append(", !");
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
