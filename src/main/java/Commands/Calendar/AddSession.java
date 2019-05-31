package Commands.Calendar;

import DataHandlers.CalendarHandler;
import Commands.Command;
import Commands.Food.LunchMessager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddSession extends Command {

    private SimpleDateFormat storesdf;


    public AddSession(){
        storesdf = new SimpleDateFormat("dd/MM/yyyy");
        this.name = "addsession";
        this.aliases = new String[]{"as"};
        this.category = "Calendar";
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1) {
            CalendarHandler calendarHandler = new CalendarHandler(e.getGuild());
            try{
                Date date = storesdf.parse(args[0]);
                ArrayList<Date> dates = calendarHandler.getSessions(false);
                Date today = new Date();
                if (date.after(new Date(today.getTime() - (1000 * 60 * 60 * 24)))) {
                    if (!dates.contains(date)) {
                        calendarHandler.addSession(date);
                        e.getChannel().sendMessage("Succesfully added a session on " + args[0]).queue();
                        LunchMessager.makeMessage(date, e.getGuild());
                    } else {
                        e.getChannel().sendMessage("There already is a session planned on " + args[0]).queue();
                    }
                } else {
                    e.getChannel().sendMessage("You cannot plan a session in the past").queue();
                }
            } catch (ParseException exc){
                e.getChannel().sendMessage("Usage: /addsession [dd/MM/yyyy]").queue();
            }

        }
        else {
            e.getChannel().sendMessage("Usage: /addsession [dd/MM/yyyy]").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Adds a session to the calendar";
    }
}
