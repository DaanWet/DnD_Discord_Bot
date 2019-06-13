package Commands.Food;

import DataHandlers.CalendarHandler;
import DataHandlers.FoodHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LunchMessager {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static Map<LocalDateTime, ScheduledFuture> map = new HashMap<>();

    public static void makeMessage(LocalDateTime date, Guild g) {
        LocalDateTime messagedate = (date == null) ? LocalDateTime.MIN : date.minusDays(1);
        long diff = messagedate.compareTo(LocalDateTime.now());
        ConfigHandler cfgh = new ConfigHandler(g);
        TextChannel ch = g.getTextChannelById(cfgh.getChannel("FoodChannel"));
        if (date == null | diff <= 0) {
            ch.sendMessage(getFood(date, g)).queue(message -> getEmojis(g).forEach(s -> message.addReaction(s).queue()));
        } else {
            ScheduledFuture<?> task = ch.sendMessage(getFood(date, g)).queueAfter(diff, TimeUnit.MILLISECONDS, message -> getEmojis(g).forEach(s -> message.addReaction(s).queue()));
            map.put(date, task);
        }
    }

    public static void cancelMessage(LocalDateTime date) {
        if (map.keySet().contains(date)) {
            map.get(date).cancel(false);
            map.remove(date);
        }
    }

    public static void onRestart(Guild g) {
        CalendarHandler calendarHandler = new CalendarHandler(g);
        ArrayList<LocalDateTime> sessions = calendarHandler.getSessions(false);
        sessions.forEach(session -> {
            LocalDateTime messagedate = session.minusDays(1);
            if (messagedate.atZone(ZoneId.systemDefault()).toEpochSecond() - System.currentTimeMillis() > 0) {
                makeMessage(session, g);
            }
        });

    }

    private static String getFood(LocalDateTime date, Guild g) {
        FoodHandler f = new FoodHandler(g);
        StringBuilder sb = new StringBuilder();
        sb.append("Wat eten we ?").append(date != null ? " Session: " + sdf.format(date) : "");
        f.getFood().forEach(food -> sb.append("\n").append(food.get("Name")).append(": ").append(food.get("Emoji")));

        return sb.toString();
    }

    private static ArrayList<String> getEmojis(Guild g) {
        FoodHandler f = new FoodHandler(g);
        ArrayList<String> emoji = new ArrayList<>();
        f.getFood().forEach(s -> emoji.add(s.get("Emoji")));
        return emoji;
    }
}
