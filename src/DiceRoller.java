import Dice.Dice;

public class DiceRoller implements ArgsListener{

    @Override
    public void run(String[] args) {
        if(args.length > 0 && args[0].matches("^[0-9]+$")){
            int value = Integer.parseInt(args[0]);
            EmbedBuilder eb = new EmbedBuilder();
            Dice dice = new Dice(value);
            eb.addField(e.getMember().getNickname() + " rolled a " + dice.getName() + " and got: ",  Integer.toString(dice.roll()), true);
            try {
                eb.setThumbnail(dice.getImage());
            } catch (Exception ex){
                // Do Nothing
            } finally {
                e.getChannel().sendMessage(eb.build()).queue();
            }
        }

    }

    @Override
    public String description() {
        return "<arg1:int> Rolls a dice with max value [arg1]";
    }
}
