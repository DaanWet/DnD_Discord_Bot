package Commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public abstract class Command {

    protected String name;
    protected String[] aliases = new String[0];
    protected String category = null;

    public abstract void run(String[] args, GuildMessageReceivedEvent e);

    public abstract String getDescription();

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }

    public boolean isCommandFor(String s){
        if (s.equalsIgnoreCase(name)) {
            return true;
        }
        for (String alias : aliases){
            if (s.equalsIgnoreCase(alias)){
                return true;
            }
        }
        return false;
    }
}
