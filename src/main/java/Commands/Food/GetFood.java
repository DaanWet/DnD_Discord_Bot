package Commands.Food;

import Commands.Command;
import DataHandlers.FoodHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class GetFood extends Command {

    public GetFood(){
        this.name = "food";
        this.aliases = new String[]{"f", "getfood", "lunch-list"};
        this.category = "Lunch";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 0){
            FoodHandler f = new FoodHandler(e.getGuild());
            EmbedBuilder eb = new EmbedBuilder();
            StringBuilder sb = new StringBuilder();
            ArrayList<Map<String, String>> foods = f.getFood();
            for (Map<String, String> food : foods){
                sb.append("\n").append(food.get("Name")).append(" ").append(food.get("Emoji"));
            }
            sb.delete(0, 1);
            eb.addField("Food", sb.toString(), true);
            //e.getChannel().sendMessage(eb.build()).queue();
            LunchMessager.makeMessage(null, e.getGuild());
        }
    }

    @Override
    public String getDescription() {
        return "Shows you the lunch-list";
    }
}
