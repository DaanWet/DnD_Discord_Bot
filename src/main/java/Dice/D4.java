package Dice;

public class D4 extends Dice {

    public D4(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d4.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(4) + 1;
    }

    @Override
    public String getDescription() {
        return "!d4: Roll a d4.";
    }
}
