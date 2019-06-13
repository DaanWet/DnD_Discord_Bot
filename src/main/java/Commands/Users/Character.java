package Commands.Users;

import Commands.Command;
import DataHandlers.CharacterHandler;
import Listeners.MessageListener;
import Players.CharacterSheet.CharacterSheetBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Map;

public class Character extends Command {

    private MessageListener messageListener;

    public Character(MessageListener l){
        this.name = "CharacterSheet";
        this.aliases = new String[]{"CS", "charsheet"};
        this.category = "Players/Users";
        messageListener = l;
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        CharacterHandler c = new CharacterHandler(e.getGuild());
        if (args.length == 0){
            messageListener.addBuilder(new CharacterSheetBuilder(e.getChannel(), e.getMember(), messageListener));
        } else{
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            if (args[0].equalsIgnoreCase("remove")){
                Map<String, String> character = c.getCharacter(sb.toString().trim(), "name");
                if (character != null){
                    c.removeCharacter(sb.toString().trim(), "name");
                    if (!character.get("userid").equals("")){
                        e.getGuild().getController().setNickname(e.getGuild().getMemberById(character.get("userid")), null).queue();
                    }
                    e.getChannel().sendMessage(String.format("Succesfully removed %s", sb.toString().trim())).queue();
                } else {
                    e.getChannel().sendMessage("Usage: /CharacterSheet <remove name>").queue();
                }
            } else if (args.length > 3 && args[0].equalsIgnoreCase("edit")){
                int index = getAttributeIndex(args);
                if (index != -1){
                    StringBuilder name = new StringBuilder();
                    for (int i = 1; i < index; i++){
                        name.append(args[i]).append(" ");
                    }
                    StringBuilder newValue = new StringBuilder();
                    for (int i = index + 1; i < args.length; i++){
                        newValue.append(args[i]).append(" ");
                    }
                    if (c.getCharacter(name.toString().trim(), "name") != null) {
                        c.editCharacter(name.toString().trim(), args[index].toLowerCase(), newValue.toString().trim());
                        e.getChannel().sendMessage(String.format("Succefully edited the attribute %s of %s to %s", args[index], name.toString().trim(), newValue.toString().trim())).queue();
                    } else {
                        e.getChannel().sendMessage("Usage: /CharacterSheet <edit name attribute newvalue>").queue();
                    }
                } else {
                    e.getChannel().sendMessage("Usage: /CharacterSheet <edit name attribute newvalue>").queue();
                }
            } else {
                e.getChannel().sendMessage("Usage: /CharacterSheet <remove name | edit name type newvalue>").queue();
            }
        }
    }

    @Override
    public String getDescription() {
        return "Starts the creation of a charactersheet";
    }

    public int getAttributeIndex(String[] args){
        ArrayList<String> attributes = CharacterSheetBuilder.getAttributes();
        int i = 0;
        boolean found = false;
        while (!found && i < args.length){
            if (attributes.contains(args[i].toLowerCase())){
                found = true;
            } else {
                i++;
            }
        }
        return found? i : -1;
    }
}
