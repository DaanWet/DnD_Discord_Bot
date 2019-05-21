package Commands.Food;

import Commands.CalendarHandler;
import Commands.Food.FoodHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LunchMessager {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static Map<Date, ScheduledFuture> map = new HashMap<>();

    public static void makeMessage(Date date, Guild g){
        Date messagedate = new Date((date == null) ? 0 : date.getTime() - 1000 * 60 * 60 * 6);
        Date today = new Date();
        StringBuilder sb = new StringBuilder();
        FoodHandler f = new FoodHandler();
        ArrayList<Map<String, String>> foods = f.getFood();
        ArrayList<String> emoji = new ArrayList<>();
        sb.append("Wat eten we ?").append(date != null ?  " Session: " + sdf.format(date) : "");
        for (Map<String, String> food : foods){
            sb.append("\n").append(food.get("Name")).append(": ").append(food.get("Emoji"));
            emoji.add(food.get("Emoji"));
        }
        long diff = messagedate.getTime() - today.getTime();
        System.out.println(diff);
        if (date == null | diff <= 0) {
            g.getTextChannelsByName("bot-test", true).get(0).sendMessage(sb.toString()).queue(message -> emoji.forEach(s -> message.addReaction(s).queue()));
        } else {
            ScheduledFuture<Message> task = g.getTextChannelsByName("bot-test", true).get(0).sendMessage(sb.toString()).submitAfter(diff, TimeUnit.MILLISECONDS);
            map.put(date, task);
        }
    }

    public static void cancelMessage(Date date){
        map.get(date).cancel(false);
        map.remove(date);
    }

    public static void onRestart(Guild g){
        List<TextChannel> t = g.getTextChannelsByName("bot-test", true);
        System.out.println(map);
        if (t.size() > 0){
            CalendarHandler calendarHandler = new CalendarHandler();
            ArrayList<Date> sessions = calendarHandler.getSessions(false);
            for (Date session : sessions){
                Date messagedate = new Date(session.getTime() - 1000 * 60 * 60 * 6);
                if (TimeUnit.HOURS.convert(messagedate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS) > 0){
                    makeMessage(session, g);
                }
            }

            System.out.println(map);
        }
    }
}
