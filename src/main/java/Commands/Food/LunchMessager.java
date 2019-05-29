package Commands.Food;

import Commands.CalendarHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LunchMessager {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static Map<Date, ScheduledFuture> map = new HashMap<>();

    public static void makeMessage(Date date, Guild g){
        Date today = new Date();
        Date messagedate = new Date((date == null) ? 0 : today.getTime() + 1000 * 60);//date.getTime() - 1000 * 60 * 60 * 6);
        long diff = messagedate.getTime() - today.getTime();
        System.out.println(diff);
        if (date == null | diff <= 0) {
            g.getTextChannelsByName("bot-test", true).get(0).sendMessage(getFood(date)).queue(message -> getEmojis().forEach(s -> message.addReaction(s).queue()));
        } else {
            ScheduledFuture<?> task =  g.getTextChannelsByName("bot-test", true).get(0).sendMessage(getFood(date)).queueAfter(diff, TimeUnit.MILLISECONDS, message -> getEmojis().forEach(s -> message.addReaction(s).queue()));
            map.put(date, task);
        }
    }

    public static void cancelMessage(Date date){
        if (map.keySet().contains(date)) {
            map.get(date).cancel(false);
            map.remove(date);
        }
    }

    public static void onRestart(Guild g){
        List<TextChannel> t = g.getTextChannelsByName("bot-test", true);
        if (t.size() > 0){
            CalendarHandler calendarHandler = new CalendarHandler();
            ArrayList<Date> sessions = calendarHandler.getSessions(false);
            for (Date session : sessions){
                Date messagedate = new Date(session.getTime() - 1000 * 60 * 60 * 6);
                if (TimeUnit.HOURS.convert(messagedate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS) > 0){
                    makeMessage(session, g);
                }
            }
        }
    }

    public static String getFood(Date date){
        FoodHandler f = new FoodHandler();
        ArrayList<Map<String, String>> foods = f.getFood();
        StringBuilder sb = new StringBuilder();
        sb.append("Wat eten we ?").append(date != null ?  " Session: " + sdf.format(date) : "");
        for (Map<String, String> food : foods){
            sb.append("\n").append(food.get("Name")).append(": ").append(food.get("Emoji"));
        }
        return sb.toString();
    }

    public static ArrayList<String> getEmojis(){
        FoodHandler f = new FoodHandler();
        ArrayList<String> emoji = new ArrayList<>();
        ArrayList<Map<String, String>> foods = f.getFood();
        foods.forEach(s -> emoji.add(s.get("Emoji")));
        return emoji;
    }
}
