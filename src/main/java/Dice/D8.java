package Dice;

public class D8 extends Dice {

    public D8(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d8.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(8) + 1;
    }

    @Override
    public String getDescription() {
        return "!d8: Roll a d8.";
    }
}
