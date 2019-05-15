package Dice;

public class D100 extends Dice{

    public D100(){
        super();
        image = "https://www.dnddice.com/media/wysiwyg/d10_.jpg";
    }
    @Override
    public int roll() {
        return super.random.nextInt(100) + 1;
    }

    @Override
    public String getDescription() {
        return "!d100: Roll a d100.";
    }
}
