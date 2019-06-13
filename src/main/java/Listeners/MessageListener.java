package Listeners;

import Players.CharacterSheet.CharacterSheetBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageListener extends ListenerAdapter {


    private List<CharacterSheetBuilder> builders = new ArrayList<>();
    private CommandListener commandListener = new CommandListener(this);

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        boolean notInList = true;
        int i = 0;
        while (notInList && i < builders.size()){
            if (builders.get(i).getChannel().equals(e.getChannel()) && builders.get(i).getUser().equals(e.getMember())){
                notInList = false;
            } else {
                i++;
            }
        }
        String message = e.getMessage().getContentRaw().trim();
        if (notInList) {
            String[] words = message.split(" ");
            if (message.length() > 0 && words[0].charAt(0) == '/') {
                commandListener.onCommandReceived(e);
            }
        } else {
            builders.get(i).answer(message);
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e){

    }

    public void addBuilder(CharacterSheetBuilder b) {
        builders.add(b);
    }

    public void removeBuilder(CharacterSheetBuilder b) {
        builders.remove(b);
    }

}