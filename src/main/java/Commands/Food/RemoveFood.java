package Commands.Food;

import Commands.Command;
import DataHandlers.FoodHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class RemoveFood extends Command {

    public RemoveFood() {
        this.name = "removefood";
        this.aliases = new String[]{"rf"};
        this.category = "Lunch";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1) {
            FoodHandler foodHandler = new FoodHandler(e.getGuild());
            if (isIntegerOrLong(args[0])) {
                int i = Integer.parseInt(args[0]) - 1;
                ArrayList<Map<String, String>> food = foodHandler.getFood();
                if (i > 0 && i < food.size()) {
                    succesMessage(e, foodHandler, i, food);
                } else {
                    e.getChannel().sendMessage(String.format("The lunch-list only containts %d items", food.size())).queue();
                }
            } else {
                int index = foodHandler.checkFood(args[0]);
                if (index != -1) {
                    ArrayList<Map<String, String>> food = foodHandler.getFood();
                    succesMessage(e, foodHandler, index, food);
                } else {
                    e.getChannel().sendMessage(args[0] + " is not an emoji on the lunch-list").queue();
                }
            }
        } else {
            e.getChannel().sendMessage("Usage: /removefood <emoji>").queue();
        }
    }

    private void succesMessage(GuildMessageReceivedEvent e, FoodHandler foodHandler, int index, ArrayList<Map<String, String>> food) {
        String emoji = food.get(index).get("Emoji");
        String name = food.get(index).get("Name");
        foodHandler.removeFood(index);
        e.getChannel().sendMessage(String.format("Succesfully removed %s with %s as emoji", name, emoji)).queue();
    }

    @Override
    public String getDescription() {
        return "Remove food from the lunch-list";
    }
}
