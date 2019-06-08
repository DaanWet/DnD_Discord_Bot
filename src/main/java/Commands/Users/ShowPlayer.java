package Commands.Users;

import Commands.Command;
import DataHandlers.CharacterHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShowPlayer extends Command {

    public ShowPlayer() {
        this.name = "ShowPlayer";
        this.aliases = new String[]{"SeePlayer", "sp"};
        this.category = "Players/Users";
    }

    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        ArrayList<Map<String, String>> list = null;
        CharacterHandler characterHandler = new CharacterHandler(e.getGuild());
        if (args.length == 0) {
            args = new String[]{"all"};
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            list = characterHandler.getAllCharacters(false);
            sendIfOne(list, e.getChannel());
        } else if (args.length == 1 && args[0].equalsIgnoreCase("npc")) {
            list = characterHandler.getAllCharacters(true);
            sendIfOne(list, e.getChannel());
        } else {
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg).append(" ");
            }
            sb.deleteCharAt(sb.length() - 1);
            Map<String, String> character = characterHandler.getCharacter(sb.toString(), "name");
            if (character == null) {
                character = characterHandler.getCharacter(sb.toString(), "userid");
            }
            if (character != null){
                list = new ArrayList<>(Collections.singletonList(character));
            }

        }
        if (list != null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.ORANGE);
            if (list.size() != 1) {
                eb.setColor(Color.ORANGE);
                eb.setTitle("Players");
                for (Map<String, String> map : list) {
                    eb.addField(map.get("name"), (map.get("userid").equals("")) ? "NPC" : e.getGuild().getMemberById(map.get("userid")).getAsMention(), true);
                }
            /*int i = 0;
            while (i < list.size()){
                eb.addField(list.get(i).get("name"), (list.get(i).get("userid").equals("")) ? "NPC" : e.getGuild().getMemberById(list.get(i).get("userid")).getAsMention(), true);
                if (i + 1< list.size()){
                    eb.addField(list.get(i + 1).get("name"), (list.get(i + 1).get("userid").equals("")) ? "NPC" : e.getGuild().getMemberById(list.get(i + 1).get("userid")).getAsMention(), true);
                }
                i += 2;
                eb.addField("", "", false);
            }*/
            } else {
                for (String key : list.get(0).keySet()) {
                    String capitalized = key.substring(0, 1).toUpperCase() + key.substring(1);
                    eb.addField(capitalized, list.get(0).get(key), true);
                }
            }
            e.getChannel().sendMessage(eb.build()).queue();
        } else {
            e.getChannel().sendMessage("Usage: /ShowPlayer [all|playername]").queue();
        }

    }


    @Override
    public String getDescription() {
        return "Shows a certain (or all) player(s)";
    }
    public void sendIfOne(ArrayList<Map<String,String>> m, TextChannel c){
        if (m.size() == 1) {
            c.sendMessage("There is only one charactersheet, showing the info of that charactersheet:").queue();
        }
    }
}
