package Dice;

public class D10 extends Dice {

    public D10(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d10.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(10) + 1;
    }

    @Override
    public String getDescription() {
        return "!d10: Roll a d10.";
    }
}
