package Commands.Food;

import Commands.Food.FoodHandler;
import net.dv8tion.jda.core.entities.Guild;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class LunchMessager {


    public static void makeMessage(Date date, Guild g){
        StringBuilder sb = new StringBuilder();
        FoodHandler f = new FoodHandler();
        ArrayList<Map<String, String>> foods = f.getFood();
        ArrayList<String> emoji = new ArrayList<>();
        sb.append("Wat eten we ?");
        for (Map<String, String> food : foods){
            sb.append("\n").append(food.get("Name")).append(": ").append(food.get("Emoji"));
            emoji.add(food.get("Emoji"));
        }
        g.getTextChannelsByName("bot-test", true).get(0).sendMessage(sb.toString()).queue(message -> emoji.forEach(s -> message.addReaction(s).queue()));
    }
}
