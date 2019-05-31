package Commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Random;

public class Dice extends Command{

    private static Random random;
    private String image;
    private int max;

    public Dice(int max){
        random = new Random();
        this.max = max;
        this.name = String.format("d%d", max);
        this.image = String.format("https://www.dnddice.com/media/wysiwyg/d%s.jpg", (max != 100) ? max : "10_");
    }

    public int roll(){
        return random.nextInt(max) + 1;
    }

    @Override
    public String getDescription(){
        return String.format("Rolls a d%d", max);
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        String name = e.getMember().getEffectiveName();
        if (args.length == 0) {
            eb.addField(name + " rolled a " + getName() + " and got: ", Integer.toString(roll()), true);
        } else if (args.length == 1){
            if (args[0].equalsIgnoreCase("adv")) {
                int first = roll();
                int second = roll();
                eb.addField(name + " rolled a " + getName() + " with advantage and got:", first + " & " + second + " => " + Math.max(first, second), true);
            } else if (args[0].equalsIgnoreCase("dis")) {
                int first = roll();
                int second = roll();
                eb.addField(name + " rolled a " + getName() + " with disadvantage and got:", first + " & " + second + " => " + Math.min(first, second), true);
            } else if (isInteger(args[0])) {
                int aantal = Integer.parseInt(args[0]);
                if (aantal <= 50) {
                    int som = 0;
                    StringBuilder result = new StringBuilder();
                    int roll = roll();
                    result.append(roll);
                    som += roll;
                    for (int i = 1; i < aantal; i++) {
                        result.append(" + ");
                        roll = roll();
                        result.append(roll);
                        som += roll;
                    }
                    result.append(" = ");
                    result.append(som);
                    eb.addField(name + " rolled " + aantal + " times a " + getName() + " and got:", result.toString(), true);
                } else {
                    eb.setTitle("We currently do not support more than 50 rolls in one command");
                    e.getChannel().sendMessage(eb.build()).queue();
                    return;
                }
            }
        } else {
            e.getChannel().sendMessage("Usage: /" + getName() + " [ adv | dis | <number>]").queue();
            return;
        }
        eb.setColor(Color.ORANGE);
        eb.setThumbnail(image);
        e.getChannel().sendMessage(eb.build()).queue();
    }

}
