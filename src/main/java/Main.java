import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ResourceBundle;

public class Main {

    public static void main(String args[]) throws Exception {

        JDA jda = new JDABuilder(ResourceBundle.getBundle("Dnd").getString("Token")).build();

        jda.addEventListener(new CommandListener());
        //https://discordapp.com/oauth2/authorize?client_id=577940186755891211&permissions=134736960&scope=bot
    }
}
