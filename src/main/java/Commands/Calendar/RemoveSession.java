package Commands.Calendar;

import Commands.Command;
import Commands.Food.LunchMessager;
import DataHandlers.CalendarHandler;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

public class RemoveSession extends Command {

    private DateTimeFormatter storesdf;

    public RemoveSession(){
        storesdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.name = "removesession";
        this.aliases = new String[]{"rs", "removes"};
        this.category = "Calendar";
    }
    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        if (args.length == 1) {
            CalendarHandler calendarHandler = new CalendarHandler(e.getGuild());
            if (!args[0].equalsIgnoreCase("all")) {
                try {
                    LocalDateTime date = LocalDate.from(storesdf.parse(args[0])).atStartOfDay();
                    ArrayList<LocalDateTime> dates = calendarHandler.getSessions(true);
                    if (dates.contains(date)) {
                        calendarHandler.removeSession(date);
                        e.getChannel().sendMessage("Succesfully removed the session on " + args[0]).queue();
                        SessionReminder.cancelSession(date);
                    } else {
                        e.getChannel().sendMessage("There is no session on " + args[0]).queue();
                    }
                } catch (DateTimeParseException exc) {
                    try {
                        int count = Integer.parseInt(args[0]);
                        ArrayList<LocalDateTime> dates = calendarHandler.getSessions(false);
                        if (count > 0 && count <= dates.size()) {
                            LocalDateTime removed = dates.get(count - 1);
                            calendarHandler.removeSession(removed);
                            e.getChannel().sendMessage("Succesfully removed the session on " + storesdf.format(removed)).queue();
                            LunchMessager.cancelMessage(removed);
                        }
                    } catch (Exception excc) {
                        e.getChannel().sendMessage("Usage: /removesession [dd/MM/yyyy]").queue();
                    }
                }
            } else {
                calendarHandler.getSessions(true).forEach(calendarHandler::removeSession);
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
