package Listeners;

import DataHandlers.CharacterHandler;
import DataHandlers.NPCMessageHandler;
import Players.DM;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        JDA jda = e.getJDA();
        String[] words = e.getMessage().getContentRaw().split(" ", 1);
        Guild guild = null;
        if (isLong(words[0])) {
            try {
                guild = jda.getGuildById(words[0]);
            } catch (Exception exc) {
                e.getChannel().sendMessage("You gave a wrong serverid").queue();
            }
        }
        if (guild != null) {
            if (checkDM(e.getAuthor(), guild)) {
                addMessage(words[1], guild);
            }
        } else {
            for (Guild g : jda.getGuilds()) {
                if (checkDM(e.getAuthor(), g)) {
                    addMessage(e.getMessage().getContentRaw(), g);
                }
            }
        }
    }

    private boolean checkDM(User user, Guild guild) {
        Member m = guild.getMember(user);
        if (m != null) {
            Role dmrole = new DM(guild).getRole();
            return (m.getRoles().contains(dmrole));
        }
        return false;
    }

    public void addMessage(String message, Guild guild){
        String[] words = message.split(": ", 1);
        CharacterHandler chh = new CharacterHandler(guild);
        ArrayList<String> names = chh.getAllCharacterNames(true);
        String name = "";
        if (names.contains(words[0])){
            name = words[0];
        }
        new NPCMessageHandler(guild).addMessage(words[1], "Specific", name);

    }


    private static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
