package Commands;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Calendar extends Command{

    private SimpleDateFormat sdf;

    public Calendar(){
        sdf = new SimpleDateFormat("EE dd MMM yyyy");
        this.name = "calendar";
        this.aliases = new String[]{"cal"};
        this.category = "Calendar";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        CalendarHandler calendarHandler = new CalendarHandler();
        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder sb = new StringBuilder();
        ArrayList<String> dates = new ArrayList<>();
        calendarHandler.getSessions(false).forEach(date -> dates.add(sdf.format(date)));
        eb.addField("Next Session:", (dates.size() > 0) ? dates.get(0) : "No planned sessions yet", true);
        if (dates.size() > 1) {
            for (int i = 1; i < dates.size(); i++) {
                sb.append("\n").append(dates.get(i));
            }
            sb.delete(0, 1);
            eb.addField("Planned Sessions:", sb.toString(), false);
        }
        eb.setColor(Color.ORANGE);
        e.getChannel().sendMessage(eb.build()).queue();
    }

    @Override
    public String getDescription() {
        return "Shows the Calendar";
    }
}
