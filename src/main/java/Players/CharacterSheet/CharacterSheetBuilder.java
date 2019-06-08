package Players.CharacterSheet;

import DataHandlers.CharacterHandler;
import Listeners.MessageListener;
import Players.DM;
import Players.Player;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CharacterSheetBuilder {

    private TextChannel channel;
    private Member user;
    private MessageListener messageListener;
    private static final ArrayList<String> attributes = new ArrayList<>(Arrays.asList("userid", "name"));//"race", "class", "background", "alignment"));
    private int current;
    private Map<String, String> characterSheet = new HashMap<>();


    public CharacterSheetBuilder(TextChannel channel, Member user, MessageListener messageListener) {
        this.channel = channel;
        this.user = user;
        this.messageListener = messageListener;
        current = 0;
        channel.sendMessage("Started character creation, all commands will be disabled till character is created\nTo cancel character creation, send cancel\nDo you create this character for yourself?(Y/N)").queue();
    }

    public void answer(String message) {
        CharacterHandler characterHandler = new CharacterHandler(channel.getGuild());
        if (!message.equalsIgnoreCase("cancel")) {
            if (current == 0) {
                if (message.matches("(?i)^Y(ES)?$") || message.matches("(?i)^NO?$")) {
                    if (message.matches("(?i)^Y(ES)?$")) {
                        if (user.getRoles().contains(new DM(channel.getGuild()).getRole())) {
                            cancelBuilder("Character creation canceled, you are the DM");
                            return;
                        } else if (characterHandler.getCharacter(user.getUser().getId(), "userid") != null) {
                            channel.sendMessage("You already have a character, do you want to overwrite it? Y/N").queue();
                            current = -1;
                            return;
                        } else {
                            message = user.getUser().getId();
                        }
                    } else {
                        message = "";
                    }
                } else {
                    channel.sendMessage("Invalid input try again.").queue();
                    return;
                }
            } else if (current == -1) {
                if (message.matches("(?i)^Y(ES)?$") || message.matches("(?i)^NO?$")) {
                    if (message.matches("(?i)^Y(ES)?$")) {
                        characterHandler.removeCharacter(user.getUser().getId(), "userid");
                        current = 0;
                        answer("YES");
                        return;
                    } else {
                        cancelBuilder("Character creation canceled");
                    }
                } else {
                    channel.sendMessage("Invalid input try again.").queue();
                    return;
                }
            }
            characterSheet.put(attributes.get(current), message);
            current++;
            if (current == attributes.size()) {
                new CharacterHandler(channel.getGuild()).addCharacter(characterSheet);
                if (!characterSheet.get("userid").equalsIgnoreCase("")) {
                    channel.getGuild().getController().addRolesToMember(user, new Player(channel.getGuild()).getRole()).queue();
                    channel.getGuild().getController().setNickname(user, characterSheet.get("name")).queue();
                }
                cancelBuilder("Character created");
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.ORANGE);
                eb.setTitle("Newly Created Character:");
                for (int i = 1; i < attributes.size(); i++){
                    String capitalized = attributes.get(i).substring(0, 1).toUpperCase() + attributes.get(i).substring(1);
                    eb.addField(capitalized, characterSheet.get(attributes.get(i)) ,false);
                }
                channel.sendMessage(eb.build()).queue();
            } else {
                channel.sendMessage(String.format("What is the %s of your character", attributes.get(current))).queue();
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
}
