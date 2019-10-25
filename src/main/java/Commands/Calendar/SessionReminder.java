package Commands.Calendar;

import Commands.Food.LunchMessager;
import DataHandlers.CalendarHandler;
import DataHandlers.CharacterHandler;
import DataHandlers.ConfigHandler;
import DataHandlers.NPCMessageHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SessionReminder {

    private static DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static Map<LocalDateTime, ScheduledFuture> map = new HashMap<>();
    private static Map<LocalDateTime, ScheduledFuture> mememap = new HashMap<>();
    private static Random random = new Random();

    public static void makeMemeMessage(LocalDateTime date, Guild g){
        //Calculate Time to meme message
        LocalDateTime mDate = date.minusHours(24);
        Long mdiff = ChronoUnit.MILLIS.between(LocalDateTime.now(), mDate);

        //Send Scheduled message and add to map
        TextChannel mch = g.getTextChannelById(new ConfigHandler(g).getChannel("MemeChannel"));
        ScheduledFuture<?> mtask = mch.sendMessage("`24h Left to post your meme to make a chance to get a special reward!`").queueAfter(mdiff > 0 ? mdiff : 0, TimeUnit.MILLISECONDS);
        mememap.put(date, mtask);
    }


    public static void makeMessage(LocalDateTime date, Guild g){
        //Calculate time to messageDate
        LocalDateTime messageDate = date.minusHours(6);
        long diff = ChronoUnit.MILLIS.between(LocalDateTime.now(), messageDate);

        //Create message
        NPCMessageHandler npch = new NPCMessageHandler(g);
        ArrayList<Map<String, String>> messages = npch.getSpecificMessages();
        if (messages.size() == 0) {
            messages = npch.getBasicMessages();
        }
        int i = messages.size() > 0 ? random.nextInt(messages.size()) : -1;
        EmbedBuilder eb = new EmbedBuilder();
        String name = i != -1 ? messages.get(i).get("npc") : "";
        String custommessage = i != -1 ? messages.get(i).get("message") : "It's time to go on an adventure!";
        eb.addField(custommessage, "Don't forget our session tomorrow!", false);

        //Set picture depending on sender
        CharacterHandler chh = new CharacterHandler(g);
        if (!name.equals("") && !chh.getPicture(name, "name").equals("")){
            eb.setAuthor(name, chh.getPicture(name, "name"), chh.getPicture(name, "name"));
        } else {
            eb.setAuthor(!name.equals("") ? name : g.getSelfMember().getEffectiveName());
        }

        //Send Scheduled Message and add to map
        TextChannel ch = g.getTextChannelById(new ConfigHandler(g).getChannel("CalendarChannel"));
        ScheduledFuture<?> task = ch.sendMessage(eb.build()).queueAfter(diff > 0 ? diff : 0, TimeUnit.MILLISECONDS, message -> {
            LunchMessager.makeMessage(date, g);
            npch.clearSpecific();
        });
        map.put(date, task);
    }

    public static void cancelSession(LocalDateTime date){
        if (map.keySet().contains(date)) {
            map.get(date).cancel(false);
            map.remove(date);
            mememap.get(date).cancel(false);
            mememap.remove(date);
        }
    }

    public static void onRestart(Guild g){
        CalendarHandler calendarHandler = new CalendarHandler(g);
        ArrayList<LocalDateTime> sessions = calendarHandler.getSessions(false);
        sessions.forEach(session -> {
            LocalDateTime memeDate = session.minusHours(24);
            LocalDateTime messageDate = session.minusHours(4);
            long diff = ChronoUnit.MILLIS.between(LocalDateTime.now(), messageDate);
            long difffood = ChronoUnit.MILLIS.between(LocalDateTime.now(), session);
            if (diff > 0) {
                makeMessage(session, g);
            } else if (difffood > 0){
                LunchMessager.makeMessage(session, g);
            }
        });
    }
}
