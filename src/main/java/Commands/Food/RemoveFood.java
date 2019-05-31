package Commands.Food;

import Commands.Command;
import DataHandlers.FoodHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class RemoveFood extends Command {

    public RemoveFood(){
        this.name = "removefood";
        this.aliases = new String[]{"rf"};
        this.category = "Lunch";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1){
            FoodHandler foodHandler = new FoodHandler(e.getGuild());
            if (isInteger(args[0])){
                int i = Integer.parseInt(args[0]) - 1;
                ArrayList<Map<String, String>> food = foodHandler.getFood();
                if (i > 0 && i < food.size()) {
                    String emoji = food.get(i).get("Emoji");
                    String name = food.get(i).get("Name");
                    foodHandler.removeFood(i);
                    e.getChannel().sendMessage("Succesully removed " + name + " with " + emoji + " as emoji").queue();
                } else {
                    e.getChannel().sendMessage("The lunch-list only containts " + food.size() + " items").queue();
                }
            } else {
                int index = foodHandler.checkFood(args[0]);
                if (index != -1) {
                    ArrayList<Map<String, String>> food = foodHandler.getFood();
                    String emoji = food.get(index).get("Emoji");
                    String name = food.get(index).get("Name");
                    foodHandler.removeFood(index);
                    e.getChannel().sendMessage("Succesully removed " + name + " with " + emoji + " as emoji").queue();
                } else {
                    e.getChannel().sendMessage(args[0] + " is not an emoji on the lunch-list").queue();
                }
            }
        } else {
            e.getChannel().sendMessage("Usage: /removefood <emoji>").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Remove food from the lunch-list";
    }
}
