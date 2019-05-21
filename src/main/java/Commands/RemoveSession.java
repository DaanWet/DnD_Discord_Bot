package Commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RemoveSession extends Command {

    private SimpleDateFormat storesdf;

    public RemoveSession(){
        storesdf = new SimpleDateFormat("dd/MM/yyyy");
        this.name = "removesession";
        this.aliases = new String[]{"rs", "removes"};
        this.category = "Calendar";
    }
    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1) {
            CalendarHandler calendarHandler = new CalendarHandler();
            if (!args[0].equalsIgnoreCase("all")) {
                try {
                    Date date = storesdf.parse(args[0]);
                    ArrayList<Date> dates = calendarHandler.getSessions(true);
                    if (dates.contains(date)) {
                        calendarHandler.removeSession(date);
                        e.getChannel().sendMessage("Succesfully removed the session on " + args[0]).queue();
                    } else {
                        e.getChannel().sendMessage("There is no session on " + args[0]).queue();
                    }
                } catch (ParseException exc) {
                    try {
                        int count = Integer.parseInt(args[0]);
                        ArrayList<Date> dates = calendarHandler.getSessions(false);
                        if (count > 0 && count <= dates.size()) {
                            Date removed = dates.get(count - 1);
                            calendarHandler.removeSession(removed);
                            e.getChannel().sendMessage("Succesfully removed the session on " + storesdf.format(removed)).queue();
                        }
                    } catch (Exception excc) {
                        e.getChannel().sendMessage("Usage: /removesession [dd/MM/yyyy]").queue();
                    }
                }
            } else {
                ArrayList<Date> dates = calendarHandler.getSessions(true);
                for (Date d : dates){
                    calendarHandler.removeSession(d);
                }
                e.getChannel().sendMessage("Succesfully removed all sessions").queue();
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
