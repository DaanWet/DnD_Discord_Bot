package Commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

    void run(String[] args, GuildMessageReceivedEvent e);

    String getDescription();
}
