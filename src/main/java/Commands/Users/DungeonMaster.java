package Commands.Users;

import Commands.Command;
import Players.DM;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.util.List;

public class DungeonMaster extends Command {

    public DungeonMaster(){
        this.name = "dm";
        this.aliases = new String[]{"DungeonMaster"};
        this.category = "Players/Users";
    }


    @Override
    public void run(String[] args, GuildMessageReceivedEvent e) {
        DM dm = new DM(e.getGuild());
        GuildController gc = e.getGuild().getController();
        if (args.length == 0) {
            if (e.getGuild().getMembersWithRoles(dm.getRole()).size() == 0) {
                gc.addRolesToMember(e.getMember(), dm.getRole()).queue();
            } else {
                e.getChannel().sendMessage("There already is a Dungeon Master").queue();
            }
        } else if (args.length == 1){
            List<Member> dmlist = e.getGuild().getMembersWithRoles(dm.getRole());
            if (dmlist.size() > 0 && dmlist.get(0).getUser().equals(e.getAuthor())){
                Member newDm;
                if (isIntegerOrLong(args[0])) {
                    newDm = e.getGuild().getMemberById(args[0]);
                } else if (e.getMessage().getMentionedMembers().size() > 0){
                    newDm = e.getMessage().getMentionedMembers().get(0);
                } else {
                    List<Member> members = e.getGuild().getMembersByName(args[0], true);
                    if (members.size() == 0){
                        members = e.getGuild().getMembersByNickname(args[0], true);
                    }
                    if (members.size() > 0){
                        newDm = members.get(0);
                    } else {
                        newDm = null;
                    }
                }
                if (newDm != null) {
                    gc.addRolesToMember(newDm, dm.getRole()).queue();
                    gc.removeRolesFromMember(e.getMember(), dm.getRole()).queue();
                } else {
                    e.getChannel().sendMessage("The member you provided doesn't exist or isn't part of the server").queue();
                }
            } else {
                e.getChannel().sendMessage("You are not the dm so you can't give it to someone else").queue();
            }
        } else {
            e.getChannel().sendMessage("Usage: /dm <UserID>").queue();
        }
    }

    @Override
    public String getDescription() {
        return "Claims or passes DM role";
    }
}
