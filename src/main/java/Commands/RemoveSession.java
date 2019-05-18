package Commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RemoveSession implements Command {

    private SimpleDateFormat storesdf;

    public RemoveSession(){
        storesdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1) {
            CalendarHandler calendarHandler = new CalendarHandler();
            try {
                Date date = storesdf.parse(args[0]);
                ArrayList<Date> dates = calendarHandler.getSessions();
                if (dates.contains(date)) {
                    calendarHandler.removeSession(date);
                    e.getChannel().sendMessage("Succesfully removed the session on " + args[0]).queue();
                } else {
                    e.getChannel().sendMessage("There is no session on " + args[0]).queue();
                }
            } catch (ParseException exc) {
                exc.printStackTrace();
            }
        } else {
            e.getChannel().sendMessage("Usage: /removesession [dd/MM/yyyy]").queue();
        }

    }

    @Override
    public String getDescription() {
        return "Removes a Session from the Calendar";
    }
}
