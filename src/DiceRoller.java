import Dice.Dice;

public class DiceRoller implements ArgsListener{

    @Override
    public void run(String[] args) {
        if(args.length > 0 && args[0].matches("^[0-9]*[dD][0-9]+$")){
            String[] splitted = args[0].split("[dD]");
            int amount = Integer.parseInt(splitted[0]);
            int value = Integer.parseInt(splitted[1]);
            EmbedBuilder eb = new EmbedBuilder();
            Dice dice = new Dice(value);
            String values = Integer.toString(dice.roll());
            for(int i = 1; i < amount; i++){
                values += ", " + dice.roll();
            }
            eb.addField(e.getMember().getNickname() + " rolled " + amount + " " + dice.getName() + " and got: ",  values, true);
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
        return "{1}d{2} Rolls {1} dice with max value {2}";
    }
}
