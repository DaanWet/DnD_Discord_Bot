package Dice;

public class D6 extends Dice{

    public D6(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d6.jpg";
    }

    @Override
    public int roll() {
        return super.random.nextInt(6) + 1;
    }

    @Override
    public String getDescription() {
        return "!d6: Roll a d6.";
    }
}
