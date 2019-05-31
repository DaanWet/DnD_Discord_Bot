package Commands.Food;

import Commands.Command;
import DataHandlers.FoodHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class AddFood extends Command {


    public AddFood(){
        this.name = "addfood";
        this.aliases = new String[]{"af"};
        this.category = "Lunch";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 2){
            FoodHandler foodHandler = new FoodHandler(e.getGuild());
            int index = foodHandler.checkFood(args[1]);
            if (index == -1) {
                foodHandler.addFood(args[0], args[1]);
                e.getChannel().sendMessage("Succesfully added " + args[0] + " to the lunch-list with " + args[1] + " as emoji").queue();
            } else {
                e.getChannel().sendMessage("The emoji " + args[1] + " already is part of the lunch-list").queue();
            }
        } else {
            e.getChannel().sendMessage("Usage: /addfood <Name> <Emoji>").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Adds a certain type of food to the lunch-list";
    }


}
