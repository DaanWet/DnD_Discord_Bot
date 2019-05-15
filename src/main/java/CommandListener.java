import Dice.*;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.annotation.processing.SupportedSourceVersion;
import java.lang.reflect.Array;
import java.util.Map;

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
    public void onGuildMessageReceived(GuildMessageReceivedEvent e){
        String message = e.getMessage().getContentRaw();
        String[] words = message.split(" ");
        if (message.length() > 0 && words[0].substring(0,1).equals("!")){
            String command = words[0].substring(1);
            if (dices.keySet().contains(command.toLowerCase())){
                EmbedBuilder eb = new EmbedBuilder();
                Dice dice = dices.get(command.toLowerCase());
                String name = (e.getMember().getNickname() != null)? e.getMember().getNickname() : e.getMember().getEffectiveName();
                eb.addField(name + " rolled a " + command.toLowerCase() + " and got: ",  Integer.toString(dice.roll()), true);
                eb.setThumbnail(dice.getImage());
                e.getChannel().sendMessage(eb.build()).queue();
            } else if (command.equalsIgnoreCase("commands") && words.length == 1){
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Sumarville Commands");
                StringBuilder dicerolls = new StringBuilder();
                for (String dice : dices.keySet()){
                    dicerolls.append(", !");
                    dicerolls.append(dice);
                }
                dicerolls.delete(0, 1);
                eb.addField("Dice-rolls", dicerolls.toString(), true);
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }
    }
}
