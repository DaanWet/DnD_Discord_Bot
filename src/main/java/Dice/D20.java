package Dice;

import java.util.Random;

public class D20 extends Dice {

    public D20(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d20.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(20) + 1;
    }

    @Override
    public String getDescription() {
        return "!d20: Roll a d20.";
    }
}
