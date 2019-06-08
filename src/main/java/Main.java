import Commands.Food.LunchMessager;
import Listeners.MessageListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

/**
 * https://discordapp.com/api/oauth2/authorize?client_id=577940186755891211&permissions=470281280&scope=bot
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JDA jda = new JDABuilder(args[0]).build();
        jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(Game.GameType.LISTENING, "/commands"));
        jda.setAutoReconnect(true);
        jda.addEventListener(new MessageListener());
        jda.awaitReady();
        jda.getGuilds().forEach(LunchMessager::onRestart);
    }
}
