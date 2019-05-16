import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.ResourceBundle;

public class Main {

    public static void main(String args[]) throws Exception {

        JDA jda = new JDABuilder(ResourceBundle.getBundle("Dnd").getString("Token")).build();
        List<Guild> guilds = jda.getGuilds();
        jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(Game.GameType.LISTENING, "!commands"));
        jda.setAutoReconnect(true);
        jda.addEventListener(new CommandListener());
        //https://discordapp.com/oauth2/authorize?client_id=577940186755891211&permissions=134736960&scope=bot
    }
}
