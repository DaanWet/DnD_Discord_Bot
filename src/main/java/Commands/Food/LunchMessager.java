package Commands.Food;

import DataHandlers.CalendarHandler;
import DataHandlers.ConfigHandler;
import DataHandlers.FoodHandler;
import DataHandlers.NPCMessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LunchMessager {

    private static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Map<LocalDateTime, ScheduledFuture> map = new HashMap<>();
    private static Random random = new Random();

    public static void makeMessage(LocalDateTime date, Guild g) {
        LocalDateTime messagedate = (date == null) ? LocalDateTime.now() : date;
        long diff = ChronoUnit.MILLIS.between(LocalDateTime.now(), messagedate);
        TextChannel ch = g.getTextChannelById(new ConfigHandler(g).getChannel("FoodChannel"));
        ScheduledFuture<?> task = ch.sendMessage(getFood(date, g)).queueAfter(diff > 0 ? diff : 0, TimeUnit.MILLISECONDS, message -> getEmojis(g).forEach(s -> message.addReaction(s).queue()));
        map.put(date, task);

    }

    public static void cancelMessage(LocalDateTime date) {
        if (map.keySet().contains(date)) {
            map.get(date).cancel(false);
            map.remove(date);
        }
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
