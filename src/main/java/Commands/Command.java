package Commands;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public abstract class Command {

    protected String name;
    protected String[] aliases = new String[0];
    protected String category = null;

    public abstract void run(String[] args, GuildMessageReceivedEvent e);

    public abstract String getDescription();

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getCategory() {
        return category;
    }

    public boolean isCommandFor(String s) {
        if (s.equalsIgnoreCase(name)) {
            return true;
        }

        int ctr = 0;
        while (ctr < aliases.length && !s.equalsIgnoreCase(aliases[ctr])) {
            ctr++;
        }

        return ctr < aliases.length;
    }

    protected boolean isIntegerOrLong(String s) {
        return isInteger(s) || isLong(s);
    }

    /**
     * Deze manier is 20-30 keer sneller dan parseInt().
     * Bron: https://stackoverflow.com/a/237204
     */
    private static boolean isInteger(String s) {
        if (s == null) {
            return false;
        }
        int length = s.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (s.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
