package Players.CharacterSheet;

import DataHandlers.CharacterHandler;
import Listeners.MessageListener;
import Players.DM;
import Players.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CharacterSheetBuilder {

    private TextChannel channel;
    private Member user;
    private MessageListener messageListener;
    private static final String[] attributes = new String[]{"userid", "name", "race", "class", "background", "alignment"};
    private int current;
    private Map<String, String> characterSheet = new HashMap<>();
    private String lastMessage;


    public CharacterSheetBuilder(TextChannel channel, Member user, MessageListener messageListener) {
        this.channel = channel;
        this.user = user;
        this.messageListener = messageListener;
        current = 0;
        channel.sendMessage("Started character creation, all commands will be disabled till character is created\nTo cancel character creation, send cancel\nDo you create this character for yourself?(Y/N)").queue(m -> setLastMessage(m.getId()));
    }

    public void answer(String message) {
        channel.deleteMessageById(lastMessage).queue();
        channel.deleteMessageById(channel.getLatestMessageId()).queue();
        CharacterHandler characterHandler = new CharacterHandler(channel.getGuild());
        if (!message.equalsIgnoreCase("cancel")) {
            if (current == 0) {
                if (message.matches("(?i)^Y(ES)?$")) {
                    if (user.getRoles().contains(new DM(channel.getGuild()).getRole())) {
                        cancelBuilder("Character creation canceled, you are the DM");
                        return;
                    } else if (characterHandler.getCharacter(user.getUser().getId(), "userid") != null) {
                        channel.sendMessage("You already have a character, do you want to overwrite it? Y/N?").queue(m -> setLastMessage(m.getId()));
                        current = -1;
                        return;
                    } else {
                        message = user.getUser().getId();
                    }
                } else if (message.matches("(?i)^NO?$")) {
                    message = "";
                } else {
                    channel.sendMessage("Invalid input try again.").queue(m -> setLastMessage(m.getId()));
                    return;
                }
            } else if (current == -1) {
                if (message.matches("(?i)^Y(ES)?$")) {
                    characterHandler.removeCharacter(user.getUser().getId(), "userid");
                    current = 0;
                    answer("YES");
                    return;

                } else if (message.matches("(?i)^NO?$")) {
                    cancelBuilder("Character creation canceled");
                } else {
                    channel.sendMessage("Invalid input try again.").queue(m -> setLastMessage(m.getId()));
                    return;
                }
            }
            characterSheet.put(attributes[current], message);
            current++;
            if (current == attributes.length) {
                new CharacterHandler(channel.getGuild()).addCharacter(characterSheet);
                if (!characterSheet.get("userid").equalsIgnoreCase("")) {
                    channel.getGuild().getController().addRolesToMember(user, new Player(channel.getGuild()).getRole()).queue();
                    channel.getGuild().getController().setNickname(user, characterSheet.get("name")).queue();
                }
                cancelBuilder("Character created");
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setTitle("Newly Created Character:");
                for (String attribute : attributes) {
                    String capitalized = attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
                    eb.addField(capitalized, characterSheet.get(attribute), true);
                }
                channel.sendMessage(eb.build()).queue();
            } else {
                channel.sendMessage(String.format("What is the %s of your character?", attributes[current])).queue(m -> setLastMessage(m.getId()));
            }
        } else {
            cancelBuilder("Character creation canceled");
        }
    }

    public void cancelBuilder(String message) {
        channel.sendMessage(message).queue();
        messageListener.removeBuilder(this);
    }

    public TextChannel getChannel() {
        return channel;
    }

    public Member getUser() {
        return user;
    }

    public void setLastMessage(String id) {
        lastMessage = id;
    }
}
