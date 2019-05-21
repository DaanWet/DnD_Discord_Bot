import Commands.CalendarHandler;
import Commands.Food.FoodHandler;
import Commands.Food.LunchMessager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class Main {

    public static void main(String args[]) throws Exception {
        JDA jda = new JDABuilder(ResourceBundle.getBundle("Dnd").getString("Token")).build();
        jda.getPresence().setPresence(OnlineStatus.ONLINE, Game.of(Game.GameType.LISTENING, "/commands"));
        jda.setAutoReconnect(true);
        jda.addEventListener(new CommandListener());
        LunchMessager.onRestart(jda.getGuilds().get(0));
        //https://discordapp.com/oauth2/authorize?client_id=577940186755891211&permissions=134736960&scope=bot
    }
}
